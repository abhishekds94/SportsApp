package com.sportsapp.feature.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.util.PagingController
import com.sportsapp.core.common.ui.toLoadState
import com.sportsapp.core.common.ui.LoadState
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.GetTeamsByLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaguesUiState())
    val uiState: StateFlow<LeaguesUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    private val initialSize = 8
    private val pageSize = 8

    fun onSportSelected(sport: String) {
        if (_uiState.value.selectedSport == sport) {
            _uiState.update { LeaguesUiState() }
            loadJob?.cancel()
            return
        }

        val leagues = Constants.Sports.SPORT_LEAGUES[sport] ?: emptyList()
        _uiState.update {
            LeaguesUiState(
                selectedSport = sport,
                availableLeagues = leagues,
                selectedLeague = null,
                allTeams = emptyList(),
                displayedTeams = emptyList(),
                isLoadingTeams = false,
                isLoadingMore = false,
                hasMoreTeams = false,
                currentPage = 0,
                errorTitle = null,
                errorMessage = null,
                errorAction = null
            )
        }
    }

    fun onLeagueSelected(leagueName: String) {
        loadJob?.cancel()
        _uiState.update {
            it.copy(
                selectedLeague = leagueName,
                allTeams = emptyList(),
                displayedTeams = emptyList(),
                currentPage = 0,
                hasMoreTeams = false,
                isLoadingTeams = true,
                isLoadingMore = false,
                errorTitle = null,
                errorMessage = null,
                errorAction = null
            )
        }
        loadTeamsForLeague(leagueName)
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingTeams || state.isLoadingMore || !state.hasMoreTeams) return

        _uiState.update { it.copy(isLoadingMore = true) }

        val page = paging.loadMore()

        _uiState.update {
            it.copy(
                isLoadingMore = false,
                displayedTeams = page.shown,
                hasMoreTeams = page.hasMore
            )
        }
    }

    private val paging = PagingController<Team>(
        initialSize = initialSize,
        pageSize = pageSize
    )

    private fun loadTeamsForLeague(leagueName: String) {
        loadJob = viewModelScope.launch {
            // start loading
            _uiState.update {
                it.copy(
                    isLoadingTeams = true,
                    isLoadingMore = false,
                    errorTitle = null,
                    errorMessage = null,
                    errorAction = null,
                    errorThrowable = null,
                    allTeams = emptyList(),
                    displayedTeams = emptyList(),
                    currentPage = 0,
                    hasMoreTeams = false
                )
            }

            getTeamsByLeagueUseCase(leagueName)
                .collectLatest { result: DomainResult<List<Team>> ->
                    val state: LoadState<List<Team>> = result.toLoadState(
                        defaultErrorTitle = "Failed to load teams",
                        isEmpty = { it.isEmpty() },
                        emptyTitle = "No teams",
                        emptyMessage = "No teams found for this league."
                    )

                    when (state) {
                        LoadState.Idle -> Unit
                        LoadState.Loading -> Unit

                        is LoadState.Success -> {
                            val page = paging.reset(state.data)
                            _uiState.update {
                                it.copy(
                                    isLoadingTeams = false,
                                    isLoadingMore = false,
                                    allTeams = page.all,
                                    displayedTeams = page.shown,
                                    hasMoreTeams = page.hasMore,
                                    currentPage = 0,
                                    errorTitle = null,
                                    errorMessage = null,
                                    errorAction = null,
                                    errorThrowable = null
                                )
                            }
                        }

                        is LoadState.Empty -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingTeams = false,
                                    isLoadingMore = false,
                                    allTeams = emptyList(),
                                    displayedTeams = emptyList(),
                                    hasMoreTeams = false,
                                    currentPage = 0,
                                    errorTitle = state.ui.title,
                                    errorMessage = state.ui.message,
                                    errorAction = null,
                                    errorThrowable = null
                                )
                            }
                        }

                        is LoadState.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingTeams = false,
                                    isLoadingMore = false,
                                    errorTitle = state.ui.title,
                                    errorMessage = state.ui.message,
                                    errorAction = state.ui.action,
                                    errorThrowable = state.throwable
                                )
                            }
                        }
                    }
                }

        }
    }
}

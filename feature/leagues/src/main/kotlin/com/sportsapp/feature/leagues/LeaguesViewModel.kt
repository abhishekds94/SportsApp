package com.sportsapp.feature.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.result.DomainResult
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
        if (!state.canLoadMore) return

        _uiState.update { it.copy(isLoadingMore = true) }

        val nextCount = (state.displayedTeams.size + pageSize).coerceAtMost(state.allTeams.size)
        val newDisplayed = state.allTeams.take(nextCount)
        val hasMore = nextCount < state.allTeams.size

        _uiState.update {
            it.copy(
                displayedTeams = newDisplayed,
                hasMoreTeams = hasMore,
                isLoadingMore = false,
                currentPage = it.currentPage + 1
            )
        }
    }

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
                .collectLatest { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            val all = result.data
                            val initial = all.take(initialSize)

                            _uiState.update {
                                it.copy(
                                    allTeams = all,
                                    displayedTeams = initial,
                                    hasMoreTeams = all.size > initialSize,
                                    isLoadingTeams = false,
                                    isLoadingMore = false,
                                    currentPage = 0,
                                    errorTitle = null,
                                    errorMessage = null,
                                    errorAction = null,
                                    errorThrowable = null
                                )
                            }
                        }

                        is DomainResult.Error -> {
                            val appError = ErrorMapper.toAppError(result.throwable)
                            val ui = ErrorMapper.toUiMessage(appError)

                            _uiState.update {
                                it.copy(
                                    isLoadingTeams = false,
                                    isLoadingMore = false,
                                    errorTitle = ui.title,
                                    errorMessage = ui.message,
                                    errorAction = ui.action,
                                    errorThrowable = result.throwable
                                )
                            }
                        }
                    }
                }
        }
    }
}

package com.sportsapp.feature.leagues

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.extensions.asResult
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaguesUiState())
    val uiState: StateFlow<LeaguesUiState> = _uiState.asStateFlow()

    fun onSportSelected(sport: String) {
        if (_uiState.value.selectedSport == sport) {
            // Deselect if clicking same sport - reset everything
            _uiState.update { LeaguesUiState() }
        } else {
            // Select new sport - reset and load leagues
            val leagues = Constants.Sports.SPORT_LEAGUES[sport] ?: emptyList()
            _uiState.update {
                LeaguesUiState(
                    selectedSport = sport,
                    availableLeagues = leagues,
                    selectedLeague = null,
                    allTeams = emptyList(),
                    displayedTeams = emptyList()
                )
            }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun onLeagueSelected(leagueName: String) {
        // Reset teams when league changes
        _uiState.update {
            it.copy(
                selectedLeague = leagueName,
                allTeams = emptyList(),
                displayedTeams = emptyList(),
                currentPage = 0,
                hasMoreTeams = false
            )
        }
        loadTeamsForLeague(leagueName)
    }

    fun loadMore() {
        val state = _uiState.value
        if (!state.canLoadMore) return

        _uiState.update { it.copy(isLoadingMore = true) }

        val nextCount = (state.displayedTeams.size + Constants.Pagination.LOAD_MORE_SIZE)
            .coerceAtMost(state.allTeams.size)

        val newTeams = state.allTeams.take(nextCount)
        val hasMore = nextCount < state.allTeams.size

        _uiState.update {
            it.copy(
                displayedTeams = newTeams,
                hasMoreTeams = hasMore,
                isLoadingMore = false
            )
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun loadTeamsForLeague(leagueName: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingTeams = true
                )
            }

            teamRepository.searchTeamsByLeague(leagueName)
                .asResult()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoadingTeams = true) }
                        }

                        is Resource.Success -> {
                            val allTeams = resource.data
                            val initialTeams = allTeams.take(Constants.Pagination.INITIAL_PAGE_SIZE)
                            val hasMore = allTeams.size > Constants.Pagination.INITIAL_PAGE_SIZE

                            _uiState.update {
                                it.copy(
                                    allTeams = allTeams,
                                    displayedTeams = initialTeams,
                                    currentPage = 0,
                                    hasMoreTeams = hasMore,
                                    isLoadingTeams = false,
                                )
                            }
                        }

                        is Resource.Error -> {
                            val appError = resource.throwable?.let(ErrorMapper::toAppError)
                                ?: com.sportsapp.core.common.error.AppError.Unknown
                            val ui = ErrorMapper.toUiMessage(appError)

                            _uiState.update {
                                it.copy(
                                    isLoadingTeams = false,
                                    errorTitle = ui.title,
                                    errorMessage = ui.message,
                                    errorAction = ui.action,
                                )
                            }
                        }
                    }
                }
        }
    }
}
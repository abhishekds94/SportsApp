package com.sportsapp.feature.teamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.AppError
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.ui.LoadState
import com.sportsapp.core.common.ui.toLoadState
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.GetTeamByNameUseCase
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
class TeamDetailViewModel @Inject constructor(
    private val getTeamByNameUseCase: GetTeamByNameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamName: String = savedStateHandle["teamName"] ?: ""

    private val _uiState = MutableStateFlow(TeamDetailUiState())
    val uiState: StateFlow<TeamDetailUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoadingTeam = true,
                    errorTitle = null,
                    errorMessage = null,
                    errorAction = null
                )
            }

            getTeamByNameUseCase(teamName).collectLatest { result: DomainResult<Team?> ->

                val state: LoadState<Team?> = result.toLoadState(
                    defaultErrorTitle = "Failed to load team",
                    isEmpty = { it == null },
                    emptyTitle = "Team not found",
                    emptyMessage = "Try searching again."
                )

                when (state) {
                    LoadState.Idle -> Unit
                    LoadState.Loading -> Unit

                    is LoadState.Success -> {
                        _uiState.update {
                            it.copy(
                                team = state.data,
                                isLoadingTeam = false,
                                errorTitle = null,
                                errorMessage = null,
                                errorAction = null
                            )
                        }
                    }

                    is LoadState.Empty -> {
                        _uiState.update {
                            it.copy(
                                team = null,
                                isLoadingTeam = false,
                                errorTitle = state.ui.title,
                                errorMessage = state.ui.message,
                                errorAction = null
                            )
                        }
                    }

                    is LoadState.Error -> {
                        _uiState.update {
                            it.copy(
                                team = null,
                                isLoadingTeam = false,
                                errorTitle = state.ui.title,
                                errorMessage = state.ui.message,
                                errorAction = state.ui.action
                            )
                        }
                    }
                }
            }


        }
    }
}

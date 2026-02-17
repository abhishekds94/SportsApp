package com.sportsapp.feature.teamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.AppError
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.result.DomainResult
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

            getTeamByNameUseCase(teamName)
                .collectLatest { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            val team = result.data
                            if (team == null) {
                                val ui = ErrorMapper.toUiMessage(AppError.NotFound)
                                _uiState.update {
                                    it.copy(
                                        team = null,
                                        isLoadingTeam = false,
                                        errorTitle = ui.title,
                                        errorMessage = ui.message,
                                        errorAction = ui.action
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        team = team,
                                        isLoadingTeam = false,
                                        errorTitle = null,
                                        errorMessage = null,
                                        errorAction = null
                                    )
                                }
                            }
                        }

                        is DomainResult.Error -> {
                            val appError = ErrorMapper.toAppError(result.throwable)
                            val ui = ErrorMapper.toUiMessage(appError)
                            _uiState.update {
                                it.copy(
                                    isLoadingTeam = false,
                                    errorTitle = ui.title,
                                    errorMessage = ui.message,
                                    errorAction = ui.action
                                )
                            }
                        }
                    }
                }

        }
    }
}

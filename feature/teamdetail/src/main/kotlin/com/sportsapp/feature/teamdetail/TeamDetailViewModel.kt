package com.sportsapp.feature.teamdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.extensions.asResult
import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.events.repository.EventRepository
import com.sportsapp.data.teams.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teamName: String = savedStateHandle["teamName"] ?: ""

    private val _uiState = MutableStateFlow(TeamDetailUiState())
    val uiState: StateFlow<TeamDetailUiState> = _uiState.asStateFlow()

    init {
        loadTeamDetails()
    }

    fun retry() {
        loadTeamDetails()
    }

    private fun loadTeamDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingTeam = true) }

            teamRepository.getTeamByName(teamName)
                .asResult()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.update { it.copy(isLoadingTeam = true) }
                        }
                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    team = resource.data,
                                    isLoadingTeam = false,
                                    error = if (resource.data == null) "Team not found" else null
                                )
                            }
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoadingTeam = false,
                                    error = resource.message
                                )
                            }
                        }
                    }
                }
        }
    }
}
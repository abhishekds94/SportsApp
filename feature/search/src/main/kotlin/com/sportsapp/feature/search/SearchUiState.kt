package com.sportsapp.feature.search

import com.sportsapp.data.teams.model.Team

sealed interface SearchUiState {

    data object Idle : SearchUiState

    data object Loading : SearchUiState

    data class Success(
        val teams: List<Team>
    ) : SearchUiState

    data class Error(
        val title: String = "Failed to load data",
        val message: String,
        val actionText: String? = "Retry",
        val throwable: Throwable? = null
    ) : SearchUiState
}

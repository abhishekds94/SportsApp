package com.sportsapp.feature.search

import com.sportsapp.domain.teams.model.Team

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()

    data class Success(val teams: List<Team>) : SearchUiState()

    data class ZeroState(
        val title: String,
        val message: String
    ) : SearchUiState()

    data class Error(
        val title: String,
        val message: String,
        val actionText: String? = "Retry",
        val throwable: Throwable? = null
    ) : SearchUiState()
}

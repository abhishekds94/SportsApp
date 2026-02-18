package com.sportsapp.feature.leagues

import com.sportsapp.domain.teams.model.Team

sealed interface LeaguesUiState {

    data object Idle : LeaguesUiState

    data class SportSelected(
        val sport: String,
        val leagues: LeagueListState,
        val teams: TeamsState
    ) : LeaguesUiState
}

sealed interface LeagueListState {
    data object Hidden : LeagueListState
    data object Loading : LeagueListState
    data class Ready(val items: List<String>) : LeagueListState
    data class Error(val title: String, val message: String, val action: String?) : LeagueListState
}

sealed interface TeamsState {
    data object NotSelected : TeamsState
    data object Loading : TeamsState
    data class Content(
        val selectedLeague: String,
        val all: List<Team>,
        val shown: List<Team>,
        val hasMore: Boolean,
        val isLoadingMore: Boolean
    ) : TeamsState

    data class Empty(val selectedLeague: String, val title: String, val message: String) : TeamsState
    data class Error(val selectedLeague: String?, val title: String, val message: String, val action: String?) : TeamsState
}

package com.sportsapp.feature.favorites

import com.sportsapp.domain.teams.model.Team

data class FavoritesUiState(
    val teams: List<Team> = emptyList(),
    val isLoading: Boolean = true
) {
    val isEmpty: Boolean get() = !isLoading && teams.isEmpty()
    val showList: Boolean get() = !isLoading && teams.isNotEmpty()
}

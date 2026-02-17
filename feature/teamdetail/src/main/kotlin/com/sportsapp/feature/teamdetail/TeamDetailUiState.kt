package com.sportsapp.feature.teamdetail

import com.sportsapp.data.events.model.Event
import com.sportsapp.data.teams.model.Team

data class TeamDetailUiState(
    val team: Team? = null,
    val isLoadingTeam: Boolean = false,
    val isLoadingEvents: Boolean = false,
    val error: String? = null
) {
    val isLoading: Boolean
        get() = isLoadingTeam || isLoadingEvents
}
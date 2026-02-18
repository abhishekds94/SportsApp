package com.sportsapp.feature.teamdetail

import com.sportsapp.domain.teams.model.Team

data class TeamDetailUiState(

    val team: Team? = null,

    val isLoadingTeam: Boolean = false,

    val isLoadingEvents: Boolean = false,

    val errorTitle: String? = null,

    val errorMessage: String? = null,

    val errorAction: String? = null,

    val isFollowing: Boolean = false,

    val isFollowActionInProgress: Boolean = false,
) {

    val isLoading: Boolean
        get() = isLoadingTeam || isLoadingEvents

    val showError: Boolean
        get() = !isLoading && errorMessage != null

    val showTeam: Boolean
        get() = !isLoading && team != null

    val showZeroState: Boolean
        get() = !isLoading && team == null && errorMessage == null
}

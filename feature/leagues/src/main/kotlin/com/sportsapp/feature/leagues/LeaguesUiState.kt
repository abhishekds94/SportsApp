package com.sportsapp.feature.leagues

import com.sportsapp.data.teams.model.Team

data class LeaguesUiState(
    val selectedSport: String? = null,
    val selectedLeague: String? = null,
    val availableLeagues: List<String> = emptyList(),
    val allTeams: List<Team> = emptyList(),
    val displayedTeams: List<Team> = emptyList(),
    val currentPage: Int = 0,
    val hasMoreTeams: Boolean = false,
    val isLoadingLeagues: Boolean = false,
    val isLoadingTeams: Boolean = false,
    val isLoadingMore: Boolean = false,

    val errorTitle: String? = null,
    val errorMessage: String? = null,
    val errorAction: String? = null,
    val errorThrowable: Throwable? = null
) {

    val hasError: Boolean
        get() = errorMessage != null

    val showZeroState: Boolean
        get() = !isLoadingLeagues &&
                !isLoadingTeams &&
                displayedTeams.isEmpty() &&
                !hasError &&
                (selectedSport == null || (selectedSport != null && selectedLeague == null))

    val showLeagueDropdown: Boolean
        get() = selectedSport != null && availableLeagues.isNotEmpty()

    val showTeams: Boolean
        get() = selectedLeague != null && displayedTeams.isNotEmpty() && !isLoadingTeams

    val showEmptyTeams: Boolean
        get() = selectedLeague != null && allTeams.isEmpty() && !isLoadingTeams && !hasError

    val canLoadMore: Boolean
        get() = hasMoreTeams && !isLoadingMore && !isLoadingTeams
}

package com.sportsapp.data.teams.source.remote

import com.sportsapp.core.network.api.SportsDbApi
import com.sportsapp.core.network.model.TeamDto
import javax.inject.Inject

/**
 * Remote-only data source.
 * Returns network DTOs; mapping to domain happens in Repository.
 */
class TeamRemoteDataSource @Inject constructor(
    private val api: SportsDbApi
) {
    suspend fun searchTeamsByLeague(leagueName: String): List<TeamDto> =
        api.searchTeamsByLeague(leagueName).teams.orEmpty()

    suspend fun searchTeams(query: String): List<TeamDto> =
        api.searchTeams(query).teams.orEmpty()

    suspend fun getTeamByName(teamName: String): TeamDto? =
        api.searchTeams(teamName).teams?.firstOrNull()
}

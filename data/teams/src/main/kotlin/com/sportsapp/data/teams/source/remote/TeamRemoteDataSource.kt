package com.sportsapp.data.teams.source.remote

import com.sportsapp.core.network.api.SportsDbApi
import com.sportsapp.data.teams.mapper.TeamMapper.toDomain
import com.sportsapp.data.teams.mapper.TeamMapper.toDomainList
import com.sportsapp.data.teams.model.Team
import javax.inject.Inject

class TeamRemoteDataSource @Inject constructor(
    private val api: SportsDbApi
) {
    suspend fun searchTeamsByLeague(leagueName: String): List<Team> {
        return api.searchTeamsByLeague(leagueName)
            .teams
            ?.toDomainList()
            ?: emptyList()
    }

    suspend fun searchTeams(query: String): List<Team> {
        return api.searchTeams(query)
            .teams
            ?.toDomainList()
            ?: emptyList()
    }

    suspend fun getTeamByName(teamName: String): Team? {
        return api.searchTeams(teamName)
            .teams
            ?.firstOrNull()
            ?.toDomain()
    }
}
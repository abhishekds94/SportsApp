package com.sportsapp.domain.teams.repository

import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.result.DomainResult
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    fun searchTeamsByLeague(leagueName: String): Flow<DomainResult<List<Team>>>
    fun searchTeams(query: String): Flow<DomainResult<List<Team>>>
    fun getTeamByName(teamName: String): Flow<DomainResult<Team?>>
    fun observeFavoriteTeams(): Flow<List<Team>>
    fun observeFavoriteTeamIds(): Flow<Set<String>>
    fun observeIsTeamFollowed(teamId: String): Flow<Boolean>

    suspend fun followTeam(team: Team)
    suspend fun unfollowTeam(teamId: String)
}

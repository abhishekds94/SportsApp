package com.sportsapp.data.teams.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    /**
     * Search teams by league name (e.g., "English Premier League")
     */
    fun searchTeamsByLeague(leagueName: String): Flow<Resource<List<Team>>>

    /**
     * Search teams by team name
     */
    fun searchTeams(query: String): Flow<Resource<List<Team>>>

    /**
     * Get team details by team ID
     */
    fun getTeamByName(teamName: String): Flow<Resource<Team?>>
}
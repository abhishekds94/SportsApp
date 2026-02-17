
package com.sportsapp.domain.teams.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.domain.teams.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    fun searchTeamsByLeague(leagueName: String): Flow<Resource<List<Team>>>
    fun searchTeams(query: String): Flow<Resource<List<Team>>>
    fun getTeamByName(teamName: String): Flow<Resource<Team?>>
}

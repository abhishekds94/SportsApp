package com.sportsapp.data.teams.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.model.Team
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamRemoteDataSource
) : TeamRepository {

    override fun searchTeamsByLeague(leagueName: String): Flow<Resource<List<Team>>> = flow {
        emit(Resource.Loading)
        val teams = remoteDataSource.searchTeamsByLeague(leagueName)
        emit(Resource.Success(teams))
    }

    override fun searchTeams(query: String): Flow<Resource<List<Team>>> = flow {
        emit(Resource.Loading)
        val teams = remoteDataSource.searchTeams(query)
        emit(Resource.Success(teams))
    }

    override fun getTeamByName(teamName: String): Flow<Resource<Team?>> = flow {
        emit(Resource.Loading)
        val team = remoteDataSource.getTeamByName(teamName)
        emit(Resource.Success(team))
    }
}
package com.sportsapp.data.teams.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.mapper.toDomain
import com.sportsapp.data.teams.mapper.toDomainList
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamRemoteDataSource
) : TeamsRepository {

    override fun searchTeamsByLeague(leagueName: String): Flow<Resource<List<Team>>> = flow {
        emit(Resource.Loading)
        try {
            val teams = remoteDataSource.searchTeamsByLeague(leagueName).toDomainList()
            emit(Resource.Success(teams))
        } catch (t: Throwable) {
            emit(Resource.Error(message = t.message ?: "Failed to load teams", throwable = t))
        }
    }

    override fun searchTeams(query: String): Flow<Resource<List<Team>>> = flow {
        emit(Resource.Loading)
        try {
            val teams = remoteDataSource.searchTeams(query).toDomainList()
            emit(Resource.Success(teams))
        } catch (t: Throwable) {
            emit(Resource.Error(message = t.message ?: "Failed to search teams", throwable = t))
        }
    }

    override fun getTeamByName(teamName: String): Flow<Resource<Team?>> = flow {
        emit(Resource.Loading)
        try {
            val team = remoteDataSource.getTeamByName(teamName)?.toDomain()
            emit(Resource.Success(team))
        } catch (t: Throwable) {
            emit(Resource.Error(message = t.message ?: "Failed to load team", throwable = t))
        }
    }
}

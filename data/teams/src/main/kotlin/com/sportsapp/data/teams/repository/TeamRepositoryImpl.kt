package com.sportsapp.data.teams.repository

import com.sportsapp.data.teams.mapper.toDomain
import com.sportsapp.data.teams.mapper.toDomainList
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import com.sportsapp.core.common.result.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamRemoteDataSource
) : TeamsRepository {

    override fun searchTeamsByLeague(leagueName: String): Flow<DomainResult<List<Team>>> = flow {
        try {
            val teams = remoteDataSource.searchTeamsByLeague(leagueName).toDomainList()
            emit(DomainResult.Success(teams))
        } catch (e: IOException) {
            emit(DomainResult.Error(e))
        } catch (e: HttpException) {
            emit(DomainResult.Error(e))
        } catch (e: SerializationException) {
            emit(DomainResult.Error(e))
        }
    }


    override fun searchTeams(query: String): Flow<DomainResult<List<Team>>> = flow {
        try {
            val teams = remoteDataSource.searchTeams(query).toDomainList()
            emit(DomainResult.Success(teams))
        } catch (t: Throwable) {
            emit(DomainResult.Error(t))
        }
    }

    override fun getTeamByName(teamName: String): Flow<DomainResult<Team?>> = flow {
        try {
            val team = remoteDataSource.getTeamByName(teamName)?.toDomain()
            emit(DomainResult.Success(team))
        } catch (t: Throwable) {
            emit(DomainResult.Error(t))
        }
    }
}

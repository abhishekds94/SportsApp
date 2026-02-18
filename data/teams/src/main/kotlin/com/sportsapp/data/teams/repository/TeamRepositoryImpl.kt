package com.sportsapp.data.teams.repository

import com.sportsapp.data.teams.mapper.toDomain
import com.sportsapp.data.teams.mapper.toDomainList
import com.sportsapp.data.teams.mapper.toFavoriteEntity
import com.sportsapp.data.teams.mapper.toDomain as toDomainFromEntity
import com.sportsapp.data.teams.local.FavoriteTeamsDao
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import com.sportsapp.core.common.result.DomainResult
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TeamRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamRemoteDataSource,
    private val favoritesDao: FavoriteTeamsDao
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
        val cached = favoritesDao.getByName(teamName)
        if (cached != null) {
            emit(DomainResult.Success(cached.toDomainFromEntity()))
        }

        try {
            val remote = remoteDataSource.getTeamByName(teamName)?.toDomain()
            emit(DomainResult.Success(remote))

            if (remote != null && favoritesDao.isFollowedOnce(remote.id)) {
                favoritesDao.upsert(remote.toFavoriteEntity())
            }
        } catch (t: Throwable) {
            if (cached == null) emit(DomainResult.Error(t))
        }
    }

    override fun observeFavoriteTeams(): Flow<List<Team>> =
        favoritesDao.observeAll()
            .map { entities -> entities.map { it.toDomainFromEntity() } }

    override fun observeFavoriteTeamIds(): Flow<Set<String>> =
        favoritesDao.observeIds()
            .map { it.toSet() }
            .distinctUntilChanged()

    override fun observeIsTeamFollowed(teamId: String): Flow<Boolean> =
        favoritesDao.observeIsFollowed(teamId).distinctUntilChanged()

    override suspend fun followTeam(team: Team) {
        favoritesDao.upsert(team.toFavoriteEntity())
    }

    override suspend fun unfollowTeam(teamId: String) {
        favoritesDao.deleteById(teamId)
    }
}

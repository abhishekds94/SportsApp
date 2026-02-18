package com.sportsapp.data.teams.repository

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.result.safeApiCall
import com.sportsapp.data.teams.local.FavoriteTeamsDao
import com.sportsapp.data.teams.mapper.toDomain
import com.sportsapp.data.teams.mapper.toDomainList
import com.sportsapp.data.teams.mapper.toFavoriteEntity
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.sportsapp.data.teams.mapper.toDomain as toDomainFromEntity

class TeamRepositoryImpl @Inject constructor(
    private val remoteDataSource: TeamRemoteDataSource,
    private val favoritesDao: FavoriteTeamsDao
) : TeamsRepository {

    override fun searchTeamsByLeague(leagueName: String): Flow<DomainResult<List<Team>>> = flow {
        emit(
            safeApiCall {
                remoteDataSource.searchTeamsByLeague(leagueName).toDomainList()
            }
        )
    }

    override fun searchTeams(query: String): Flow<DomainResult<List<Team>>> = flow {
        emit(
            safeApiCall {
                remoteDataSource.searchTeams(query).toDomainList()
            }
        )
    }

    override fun getTeamByName(teamName: String): Flow<DomainResult<Team?>> = flow {
        val cached = favoritesDao.getByName(teamName)
        if (cached != null) {
            emit(DomainResult.Success(cached.toDomainFromEntity()))
        }

        val remoteResult = safeApiCall {
            remoteDataSource.getTeamByName(teamName)?.toDomain()
        }

        when (remoteResult) {
            is DomainResult.Success -> {
                val remote = remoteResult.data
                emit(DomainResult.Success(remote))

                if (remote != null && favoritesDao.isFollowedOnce(remote.id)) {
                    favoritesDao.upsert(remote.toFavoriteEntity())
                }
            }
            is DomainResult.Error -> {
                if (cached == null) emit(remoteResult)
            }
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

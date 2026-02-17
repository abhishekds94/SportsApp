package com.sportsapp.data.leagues.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.leagues.model.League
import com.sportsapp.data.leagues.source.remote.LeagueRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val remoteDataSource: LeagueRemoteDataSource
) : LeagueRepository {

    override fun getAllLeagues(): Flow<Resource<List<League>>> = flow {
        emit(Resource.Loading)
        val leagues = remoteDataSource.getAllLeagues()
        emit(Resource.Success(leagues))
    }

    override fun getLeaguesByCountry(country: String): Flow<Resource<List<League>>> = flow {
        emit(Resource.Loading)
        val leagues = remoteDataSource.getLeaguesByCountry(country)
        emit(Resource.Success(leagues))
    }

    override fun getLeaguesBySport(sport: String): Flow<Resource<List<League>>> = flow {
        emit(Resource.Loading)
        val allLeagues = remoteDataSource.getAllLeagues()
        val filtered = allLeagues.filter { it.sport.equals(sport, ignoreCase = true) }
        emit(Resource.Success(filtered))
    }
}
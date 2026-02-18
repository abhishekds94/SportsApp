package com.sportsapp.data.leagues.repository

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.result.safeApiCall
import com.sportsapp.data.leagues.mapper.toDomainList
import com.sportsapp.data.leagues.source.remote.LeagueRemoteDataSource
import com.sportsapp.domain.leagues.model.League
import com.sportsapp.domain.leagues.repository.LeaguesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val remote: LeagueRemoteDataSource
) : LeaguesRepository {

    override fun getAllLeagues(): Flow<DomainResult<List<League>>> = flow {
        emit(
            safeApiCall {
                remote.getAllLeagues().toDomainList()
            }
        )
    }
}

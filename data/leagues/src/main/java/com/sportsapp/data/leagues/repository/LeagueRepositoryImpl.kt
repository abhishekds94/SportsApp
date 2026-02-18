package com.sportsapp.data.leagues.repository

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.data.leagues.mapper.toDomainList
import com.sportsapp.data.leagues.source.remote.LeagueRemoteDataSource
import com.sportsapp.domain.leagues.model.League
import com.sportsapp.domain.leagues.repository.LeaguesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import kotlinx.serialization.SerializationException
import javax.inject.Inject

class LeagueRepositoryImpl @Inject constructor(
    private val remote: LeagueRemoteDataSource
) : LeaguesRepository {

    override fun getAllLeagues(): Flow<DomainResult<List<League>>> = flow {
        try {
            val leagues = remote.getAllLeagues().toDomainList()
            emit(DomainResult.Success(leagues))
        } catch (e: IOException) {
            emit(DomainResult.Error(e))
        } catch (e: HttpException) {
            emit(DomainResult.Error(e))
        } catch (e: SerializationException) {
            emit(DomainResult.Error(e))
        }
    }
}

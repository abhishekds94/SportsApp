package com.sportsapp.domain.leagues.repository

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.leagues.model.League
import kotlinx.coroutines.flow.Flow

interface LeaguesRepository {
    fun getAllLeagues(): Flow<DomainResult<List<League>>>
}

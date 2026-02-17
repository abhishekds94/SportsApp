package com.sportsapp.data.leagues.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.leagues.model.League
import kotlinx.coroutines.flow.Flow

interface LeagueRepository {
    fun getAllLeagues(): Flow<Resource<List<League>>>
    fun getLeaguesByCountry(country: String): Flow<Resource<List<League>>>
    fun getLeaguesBySport(sport: String): Flow<Resource<List<League>>>
}
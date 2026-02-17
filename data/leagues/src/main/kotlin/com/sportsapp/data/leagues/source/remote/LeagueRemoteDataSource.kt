package com.sportsapp.data.leagues.source.remote

import com.sportsapp.core.network.api.SportsDbApi
import com.sportsapp.data.leagues.mapper.LeagueMapper.toDomainList
import com.sportsapp.data.leagues.model.League
import javax.inject.Inject

/**
 * Remote data source for leagues
 * Handles API calls and data transformation
 */
class LeagueRemoteDataSource @Inject constructor(
    private val api: SportsDbApi
) {
    /**
     * Fetch all leagues from API
     */
    suspend fun getAllLeagues(): List<League> {
        return api.getAllLeagues()
            .leagues
            ?.toDomainList()
            ?: emptyList()
    }

    /**
     * Fetch leagues by country from API
     */
    suspend fun getLeaguesByCountry(country: String): List<League> {
        return api.getLeaguesByCountry(country)
            .leagues
            ?.toDomainList()
            ?: emptyList()
    }
}
package com.sportsapp.data.leagues.source.remote

import com.sportsapp.core.network.api.SportsDbApi
import com.sportsapp.core.network.model.LeagueDto
import javax.inject.Inject

class LeagueRemoteDataSource @Inject constructor(
    private val api: SportsDbApi
) {
    suspend fun getAllLeagues(): List<LeagueDto> {
        return api.getAllLeagues().leagues.orEmpty()
    }
}

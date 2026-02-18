package com.sportsapp.core.network.api

import com.sportsapp.core.network.model.LeaguesResponse
import com.sportsapp.core.network.model.TeamsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsDbApi {

    @GET("search_all_teams.php")
    suspend fun searchTeamsByLeague(
        @Query("l") leagueName: String
    ): TeamsResponse

    @GET("searchteams.php")
    suspend fun searchTeams(
        @Query("t") teamName: String
    ): TeamsResponse

    @GET("all_leagues.php")
    suspend fun getAllLeagues(): LeaguesResponse
}
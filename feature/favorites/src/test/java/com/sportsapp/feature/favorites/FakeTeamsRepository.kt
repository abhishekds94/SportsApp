package com.sportsapp.feature.favorites

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeTeamsRepository(
    initialFavorites: List<Team> = emptyList()
) : TeamsRepository {

    private val favoritesFlow = MutableStateFlow(initialFavorites)

    var lastUnfollowedTeamId: String? = null

    override fun observeFavoriteTeams(): Flow<List<Team>> = favoritesFlow

    override suspend fun unfollowTeam(teamId: String) {
        lastUnfollowedTeamId = teamId
        favoritesFlow.value = favoritesFlow.value.filterNot { it.id == teamId }
    }

    override fun searchTeamsByLeague(leagueName: String): Flow<DomainResult<List<Team>>> =
        flowOf(DomainResult.Success(emptyList()))

    override fun searchTeams(query: String): Flow<DomainResult<List<Team>>> =
        flowOf(DomainResult.Success(emptyList()))

    override fun getTeamByName(teamName: String): Flow<DomainResult<Team?>> =
        flowOf(DomainResult.Success(null))

    override fun observeFavoriteTeamIds(): Flow<Set<String>> =
        favoritesFlow.map { teams -> teams.mapNotNull { it.id }.toSet() }

    override fun observeIsTeamFollowed(teamId: String): Flow<Boolean> =
        observeFavoriteTeamIds().map { it.contains(teamId) }

    override suspend fun followTeam(team: Team) {
        favoritesFlow.value = favoritesFlow.value + team
    }
}

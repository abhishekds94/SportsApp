package com.sportsapp.data.teams.repository

import com.sportsapp.data.teams.local.FavoriteTeamEntity
import com.sportsapp.data.teams.local.FavoriteTeamsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeFavoriteTeamsDao : FavoriteTeamsDao {

    private val items = LinkedHashMap<String, FavoriteTeamEntity>()
    private val stream = MutableStateFlow<List<FavoriteTeamEntity>>(emptyList())

    private fun emit() {
        stream.value = items.values.toList()
    }

    override suspend fun upsert(team: FavoriteTeamEntity) {
        items[team.id] = team
        emit()
    }

    override suspend fun deleteById(teamId: String) {
        items.remove(teamId)
        emit()
    }

    override fun observeAll(): Flow<List<FavoriteTeamEntity>> = stream

    override fun observeIds(): Flow<List<String>> =
        stream.map { list -> list.map { it.id } }

    override fun observeIsFollowed(teamId: String): Flow<Boolean> =
        stream.map { list -> list.any { it.id == teamId } }

    override suspend fun isFollowedOnce(teamId: String): Boolean =
        items.containsKey(teamId)

    override suspend fun getByName(teamName: String): FavoriteTeamEntity? =
        items.values.firstOrNull { it.name == teamName }
}

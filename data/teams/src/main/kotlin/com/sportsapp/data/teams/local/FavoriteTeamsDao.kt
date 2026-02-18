package com.sportsapp.data.teams.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTeamsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(team: FavoriteTeamEntity)

    @Query("DELETE FROM favorite_teams WHERE id = :teamId")
    suspend fun deleteById(teamId: String)

    @Query("SELECT * FROM favorite_teams ORDER BY name ASC")
    fun observeAll(): Flow<List<FavoriteTeamEntity>>

    @Query("SELECT id FROM favorite_teams")
    fun observeIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_teams WHERE id = :teamId)")
    fun observeIsFollowed(teamId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_teams WHERE id = :teamId)")
    suspend fun isFollowedOnce(teamId: String): Boolean

    @Query("SELECT * FROM favorite_teams WHERE name = :teamName LIMIT 1")
    suspend fun getByName(teamName: String): FavoriteTeamEntity?
}

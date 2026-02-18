package com.sportsapp.data.teams.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteTeamEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SportsDatabase : RoomDatabase() {
    abstract fun favoriteTeamsDao(): FavoriteTeamsDao
}

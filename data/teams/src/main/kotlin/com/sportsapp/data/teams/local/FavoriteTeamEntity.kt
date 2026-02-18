package com.sportsapp.data.teams.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_teams")
data class FavoriteTeamEntity(
    @PrimaryKey val id: String,
    val name: String,
    val shortName: String?,
    val sport: String?,
    val league: String?,
    val country: String?,
    val stadium: String?,
    val stadiumLocation: String?,
    val stadiumCapacity: String?,
    val badgeUrl: String?,
    val jerseyUrl: String?,
    val description: String?,
    val formedYear: String?,
    val website: String?,
    val facebook: String?,
    val twitter: String?,
    val instagram: String?,
    val youtube: String?,
    val cachedAt: Long
)

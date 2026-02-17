package com.sportsapp.domain.teams.model

/**
 * Domain model used by UI + business logic.
 * Keep this free of Android/framework/network concerns.
 */
data class Team(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val sport: String? = null,
    val league: String? = null,
    val country: String? = null,
    val stadium: String? = null,
    val stadiumLocation: String? = null,
    val stadiumCapacity: String? = null,
    val badgeUrl: String? = null,
    val jerseyUrl: String? = null,
    val description: String? = null,
    val formedYear: String? = null,
    val website: String? = null,
    val facebook: String? = null,
    val twitter: String? = null,
    val instagram: String? = null,
    val youtube: String? = null
)

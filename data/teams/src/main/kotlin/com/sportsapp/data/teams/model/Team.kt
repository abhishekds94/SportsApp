package com.sportsapp.data.teams.model

data class Team(
    val id: String,
    val name: String,
    val shortName: String?,
    val sport: String,
    val league: String?,
    val country: String?,
    val stadium: String?,
    val stadiumLocation: String?,
    val stadiumCapacity: String?,
    val badge: String?,
    val jersey: String?,
    val description: String?,
    val formedYear: String?,
    val website: String?,
    val facebook: String?,
    val twitter: String?,
    val instagram: String?,
    val youtube: String?
) {
    companion object {
        fun empty() = Team(
            id = "",
            name = "",
            shortName = null,
            sport = "",
            league = null,
            country = null,
            stadium = null,
            stadiumLocation = null,
            stadiumCapacity = null,
            badge = null,
            jersey = null,
            description = null,
            formedYear = null,
            website = null,
            facebook = null,
            twitter = null,
            instagram = null,
            youtube = null
        )
    }
}
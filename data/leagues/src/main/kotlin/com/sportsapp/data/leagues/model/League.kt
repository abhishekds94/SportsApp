package com.sportsapp.data.leagues.model

data class League(
    val id: String,
    val name: String,
    val sport: String,
    val country: String?,
    val badge: String?,
    val logo: String?,
    val description: String?
) {
    companion object {
        fun empty() = League(
            id = "",
            name = "",
            sport = "",
            country = null,
            badge = null,
            logo = null,
            description = null
        )
    }
}
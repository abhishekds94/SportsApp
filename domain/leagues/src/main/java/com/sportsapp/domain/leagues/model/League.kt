package com.sportsapp.domain.leagues.model

data class League(
    val id: String,
    val name: String,
    val sport: String?,
    val alternateName: String?
)

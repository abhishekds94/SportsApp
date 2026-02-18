package com.sportsapp.data.leagues.mapper

import com.sportsapp.core.network.model.LeagueDto
import com.sportsapp.domain.leagues.model.League

fun LeagueDto.toDomain(): League? {
    val idSafe = id?.trim().orEmpty()
    val nameSafe = name?.trim().orEmpty()
    if (idSafe.isEmpty() || nameSafe.isEmpty()) return null

    return League(
        id = idSafe,
        name = nameSafe,
        sport = sport?.trim(),
        alternateName = alternateName?.trim()
    )
}

fun List<LeagueDto>.toDomainList(): List<League> =
    mapNotNull { it.toDomain() }

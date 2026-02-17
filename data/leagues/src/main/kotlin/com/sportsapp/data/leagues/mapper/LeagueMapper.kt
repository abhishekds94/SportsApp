package com.sportsapp.data.leagues.mapper

import com.sportsapp.core.network.model.LeagueDto
import com.sportsapp.data.leagues.model.League

object LeagueMapper {
    fun LeagueDto.toDomain(): League {
        return League(
            id = id ?: "",
            name = name ?: "Unknown League",
            sport = sport ?: "",
            country = country,
            badge = badge,
            logo = logo,
            description = description
        )
    }

    fun List<LeagueDto>.toDomainList(): List<League> {
        return mapNotNull { dto ->
            if (!dto.id.isNullOrBlank() && !dto.name.isNullOrBlank()) {
                dto.toDomain()
            } else null
        }
    }
}
package com.sportsapp.data.teams.mapper

import com.sportsapp.core.network.model.TeamDto
import com.sportsapp.data.teams.model.Team

object TeamMapper {
    fun TeamDto.toDomain(): Team {
        return Team(
            id = id ?: "",
            name = name ?: "Unknown Team",
            shortName = shortName,
            sport = sport ?: "",
            league = league,
            country = country,
            stadium = stadium,
            stadiumLocation = stadiumLocation,
            stadiumCapacity = stadiumCapacity,
            badge = badge,
            jersey = jersey,
            description = description,
            formedYear = formedYear,
            website = website,
            facebook = facebook,
            twitter = twitter,
            instagram = instagram,
            youtube = youtube
        )
    }

    fun List<TeamDto>.toDomainList(): List<Team> {
        return mapNotNull { dto ->
            if (!dto.id.isNullOrBlank() && !dto.name.isNullOrBlank()) {
                dto.toDomain()
            } else null
        }
    }
}
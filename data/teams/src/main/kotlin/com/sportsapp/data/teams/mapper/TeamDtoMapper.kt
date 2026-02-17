package com.sportsapp.data.teams.mapper

import com.sportsapp.core.network.model.TeamDto
import com.sportsapp.domain.teams.model.Team

internal fun TeamDto.toDomain(): Team = Team(
    id = id.orEmpty(),
    name = name.orEmpty(),
    shortName = shortName,
    sport = sport,
    league = league,
    country = country,
    stadium = stadium,
    stadiumLocation = stadiumLocation,
    stadiumCapacity = stadiumCapacity,
    badgeUrl = badge,
    jerseyUrl = jersey,
    description = description,
    formedYear = formedYear,
    website = website,
    facebook = facebook,
    twitter = twitter,
    instagram = instagram,
    youtube = youtube
)

internal fun List<TeamDto>.toDomainList(): List<Team> = map { it.toDomain() }

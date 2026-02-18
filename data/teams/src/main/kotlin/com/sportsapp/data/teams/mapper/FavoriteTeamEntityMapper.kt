package com.sportsapp.data.teams.mapper

import com.sportsapp.data.teams.local.FavoriteTeamEntity
import com.sportsapp.domain.teams.model.Team

fun FavoriteTeamEntity.toDomain(): Team = Team(
    id = id,
    name = name,
    shortName = shortName,
    sport = sport,
    league = league,
    country = country,
    stadium = stadium,
    stadiumLocation = stadiumLocation,
    stadiumCapacity = stadiumCapacity,
    badgeUrl = badgeUrl,
    jerseyUrl = jerseyUrl,
    description = description,
    formedYear = formedYear,
    website = website,
    facebook = facebook,
    twitter = twitter,
    instagram = instagram,
    youtube = youtube
)

fun Team.toFavoriteEntity(nowMillis: Long = System.currentTimeMillis()): FavoriteTeamEntity =
    FavoriteTeamEntity(
        id = id,
        name = name,
        shortName = shortName,
        sport = sport,
        league = league,
        country = country,
        stadium = stadium,
        stadiumLocation = stadiumLocation,
        stadiumCapacity = stadiumCapacity,
        badgeUrl = badgeUrl,
        jerseyUrl = jerseyUrl,
        description = description,
        formedYear = formedYear,
        website = website,
        facebook = facebook,
        twitter = twitter,
        instagram = instagram,
        youtube = youtube,
        cachedAt = nowMillis
    )

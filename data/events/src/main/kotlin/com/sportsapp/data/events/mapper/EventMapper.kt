package com.sportsapp.data.events.mapper

import com.sportsapp.core.network.model.EventDto
import com.sportsapp.data.events.model.Event
import com.sportsapp.data.events.model.EventStatus

object EventMapper {
    fun EventDto.toDomain(): Event {
        val status = when {
            this.status?.contains("FT", ignoreCase = true) == true -> EventStatus.FINISHED
            this.status?.contains("Live", ignoreCase = true) == true -> EventStatus.LIVE
            else -> EventStatus.UPCOMING
        }

        return Event(
            id = id ?: "",
            name = name ?: "$homeTeam vs $awayTeam",
            league = league ?: "",
            homeTeam = homeTeam ?: "",
            awayTeam = awayTeam ?: "",
            homeTeamBadge = null,
            awayTeamBadge = null,
            homeScore = homeScore,
            awayScore = awayScore,
            date = date ?: "",
            time = time,
            status = status,
            venue = venue
        )
    }

    fun List<EventDto>.toDomainList(): List<Event> {
        return mapNotNull { dto ->
            if (!dto.homeTeam.isNullOrBlank() && !dto.awayTeam.isNullOrBlank()) {
                dto.toDomain()
            } else null
        }
    }
}
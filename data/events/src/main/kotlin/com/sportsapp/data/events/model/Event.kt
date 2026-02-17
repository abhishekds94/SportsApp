package com.sportsapp.data.events.model

data class Event(
    val id: String,
    val name: String,
    val league: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamBadge: String?,
    val awayTeamBadge: String?,
    val homeScore: String?,
    val awayScore: String?,
    val date: String,
    val time: String?,
    val status: EventStatus,
    val venue: String?
) {
    val isLive: Boolean
        get() = status == EventStatus.LIVE

    val isFinished: Boolean
        get() = status == EventStatus.FINISHED

    val isUpcoming: Boolean
        get() = status == EventStatus.UPCOMING

    companion object {
        fun empty() = Event(
            id = "",
            name = "",
            league = "",
            homeTeam = "",
            awayTeam = "",
            homeTeamBadge = null,
            awayTeamBadge = null,
            homeScore = null,
            awayScore = null,
            date = "",
            time = null,
            status = EventStatus.UPCOMING,
            venue = null
        )
    }
}

enum class EventStatus {
    UPCOMING,
    LIVE,
    FINISHED
}
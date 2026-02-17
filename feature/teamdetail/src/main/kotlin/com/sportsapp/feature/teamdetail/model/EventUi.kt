package com.sportsapp.feature.teamdetail.model

data class EventUi(
    val id: String,
    val name: String,
    val league: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeTeamBadge: String? = null,
    val awayTeamBadge: String? = null,
    val homeScore: String? = null,
    val awayScore: String? = null,
    val date: String,
    val time: String? = null,
    val status: EventStatusUi,
    val venue: String? = null
)

enum class EventStatusUi {
    UPCOMING,
    FINISHED
}

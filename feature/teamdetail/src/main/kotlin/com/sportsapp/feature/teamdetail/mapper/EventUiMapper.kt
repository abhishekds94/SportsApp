package com.sportsapp.feature.teamdetail.mapper

import com.sportsapp.feature.teamdetail.model.EventStatusUi
import com.sportsapp.feature.teamdetail.model.EventUi

/**
 * Placeholder mapper for when Domain Events exist.
 * Keeps mapping logic out of Composables.
 */
object EventUiMapper {
    fun fromPreview(): List<EventUi> {
        return listOf(
            EventUi(
                id = "1",
                name = "Arsenal vs Chelsea",
                league = "Premier League",
                homeTeam = "Arsenal",
                awayTeam = "Chelsea",
                homeScore = "2",
                awayScore = "1",
                date = "2026-02-10",
                time = "15:00",
                status = EventStatusUi.FINISHED,
                venue = "Emirates Stadium"
            )
        )
    }
}

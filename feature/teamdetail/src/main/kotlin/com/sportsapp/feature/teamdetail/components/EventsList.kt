package com.sportsapp.feature.teamdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportsapp.core.common.util.DateTimeUtils
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.feature.teamdetail.model.EventStatusUi
import com.sportsapp.feature.teamdetail.model.EventUi

@Composable
fun EventsList(
    events: List<EventUi>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        events.forEach { event ->
            EventCard(event = event)
        }
    }
}

@Composable
private fun EventCard(
    event: EventUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Teams and Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.homeTeam,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                val hasScore =
                    event.status == EventStatusUi.FINISHED &&
                            event.homeScore != null &&
                            event.awayScore != null

                if (hasScore) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.homeScore.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "-",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = event.awayScore.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = event.awayTeam,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = DateTimeUtils.formatDate(event.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val time = event.time
                if (!time.isNullOrBlank()) {
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Venue
            val venue = event.venue
            if (!venue.isNullOrBlank()) {
                Text(
                    text = venue,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun EventsListPreview() {
    SportsAppTheme {
        EventsList(
            events = listOf(
                EventUi(
                    id = "1",
                    name = "Arsenal vs Chelsea",
                    league = "Premier League",
                    homeTeam = "Arsenal",
                    awayTeam = "Chelsea",
                    homeTeamBadge = null,
                    awayTeamBadge = null,
                    homeScore = "2",
                    awayScore = "1",
                    date = "2026-02-10",
                    time = "15:00",
                    status = EventStatusUi.FINISHED,
                    venue = "Emirates Stadium"
                ),
                EventUi(
                    id = "2",
                    name = "Liverpool vs Arsenal",
                    league = "Premier League",
                    homeTeam = "Liverpool",
                    awayTeam = "Arsenal",
                    homeTeamBadge = null,
                    awayTeamBadge = null,
                    homeScore = null,
                    awayScore = null,
                    date = "2026-02-20",
                    time = "17:30",
                    status = EventStatusUi.UPCOMING,
                    venue = "Anfield"
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

package com.sportsapp.feature.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.component.TeamBadge
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.data.teams.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamResultItem(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                TeamBadge(
                    badgeUrl = team.badge,
                    teamName = team.name,
                    size = 36.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val sub = listOfNotNull(team.league, team.country)
                        .joinToString(" • ")
                        .ifBlank { null }

                    if (sub != null) {
                        Text(
                            text = sub,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "FOLLOWING",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamResultItemPreview() {
    SportsAppTheme {
        TeamResultItem(
            team = Team(
                id = "1",
                name = "Manchester United",
                shortName = "MUN",
                sport = "Soccer",
                league = "Premier League",
                country = "England",
                stadium = null,
                stadiumLocation = null,
                stadiumCapacity = null,
                badge = null,
                jersey = null,
                description = null,
                formedYear = null,
                website = null,
                facebook = null,
                twitter = null,
                instagram = null,
                youtube = null
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

package com.sportsapp.feature.leagues.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
fun TeamListItem(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamBadge(
                badgeUrl = team.badge,
                teamName = team.name,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                team.stadium?.let { stadium ->
                    Text(
                        text = stadium,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                team.country?.let { country ->
                    Text(
                        text = country,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ==================== PREVIEWS ====================

@Preview(showBackground = true)
@Composable
private fun TeamListItemPreview() {
    SportsAppTheme {
        TeamListItem(
            team = Team(
                id = "1",
                name = "Arsenal",
                shortName = "ARS",
                sport = "Soccer",
                league = "English Premier League",
                country = "England",
                stadium = "Emirates Stadium",
                stadiumLocation = "London",
                stadiumCapacity = "60704",
                badge = null,
                jersey = null,
                description = "Arsenal Football Club",
                formedYear = "1886",
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

@Preview(showBackground = true, name = "Long Name")
@Composable
private fun TeamListItemLongNamePreview() {
    SportsAppTheme {
        TeamListItem(
            team = Team(
                id = "2",
                name = "Wolverhampton Wanderers",
                shortName = "Wolves",
                sport = "Soccer",
                league = "English Premier League",
                country = "England",
                stadium = "Molineux Stadium",
                stadiumLocation = "Wolverhampton",
                stadiumCapacity = "32050",
                badge = null,
                jersey = null,
                description = "Wolves FC",
                formedYear = "1877",
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
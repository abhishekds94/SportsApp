package com.sportsapp.feature.teamdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.component.TeamBadge
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.domain.teams.model.Team

@Composable
fun TeamHeader(
    teamEntity: Team,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TeamBadge(
                badgeUrl = teamEntity.badgeUrl,
                teamName = teamEntity.name,
                size = 80.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = teamEntity.name,
                style = MaterialTheme.typography.headlineMedium
            )

            teamEntity.shortName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                teamEntity.league?.let { league ->
                    InfoItem(
                        label = "League",
                        value = league
                    )
                }

                teamEntity.country?.let { country ->
                    InfoItem(
                        label = "Country",
                        value = country
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamHeaderPreview() {
    SportsAppTheme {
        TeamHeader(
            teamEntity = Team(
                id = "1",
                name = "Arsenal",
                shortName = "ARS",
                sport = "Football",
                league = "Premier League",
                country = "England",
                stadium = "Emirates Stadium",
                stadiumLocation = "London",
                stadiumCapacity = "60,704",
                badgeUrl = null,
                description = "Arsenal Football Club is a professional football club.",
                formedYear = "1886",
                website = null,
                facebook = null,
                twitter = null,
                instagram = null,
                youtube = null
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
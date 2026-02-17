package com.sportsapp.feature.leagues.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.component.TeamBadge
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.domain.teams.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamGridItem(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    TeamBadge(
                        badgeUrl = team.badgeUrl,
                        teamName = team.name,
                        size = 44.dp
                    )
                }
            }

            Text(
                text = team.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamGridItemPreview() {
    SportsAppTheme {
        TeamGridItem(
            team = Team(
                id = "1",
                name = "Man City",
                shortName = "MCI",
                sport = "Soccer",
                league = "Premier League",
                country = "England",
                stadium = "Etihad Stadium",
                stadiumLocation = "Manchester",
                stadiumCapacity = "55000",
                badgeUrl = null,
                description = null,
                formedYear = null,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

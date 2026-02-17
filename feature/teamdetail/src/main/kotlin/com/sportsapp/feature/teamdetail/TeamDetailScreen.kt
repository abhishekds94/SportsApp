package com.sportsapp.feature.teamdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Stadium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportsapp.core.designsystem.component.ErrorState
import com.sportsapp.core.designsystem.component.LoadingIndicator
import com.sportsapp.core.designsystem.component.TeamBadge
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.data.teams.model.Team

@Composable
fun TeamDetailScreen(
    onBackClick: () -> Unit,
    viewModel: TeamDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeamDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = viewModel::retry
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeamDetailContent(
    uiState: TeamDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(start = 12.dp, top = 12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorState(
                    title = "Failed to load data",
                    message = uiState.error,
                    actionText = "Retry",
                    onRetry = { /* call reloadTeam() */ }
                )
                uiState.team != null -> TeamDetailBody(team = uiState.team)
            }
        }
    }
}

@Composable
private fun TeamDetailBody(
    team: Team,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 52.dp, bottom = 28.dp)
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LogoSquare(team = team)

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = team.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = listOfNotNull(team.league, team.country).joinToString(" • ").ifBlank { "-" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(22.dp))

            FollowPillButton(
                text = "+ Follow",
                onClick = { /* UI-only for now */ }
            )

            Spacer(modifier = Modifier.height(34.dp))

            SectionTitle("OVERVIEW")

            Spacer(modifier = Modifier.height(14.dp))

            OverviewCard(team = team)

            Spacer(modifier = Modifier.height(28.dp))

            SectionTitle("ABOUT ${team.name.uppercase()}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = team.description
                    ?.replace("\\r\\n", "\n")
                    ?.replace("\\n\\r", "\n")
                    ?.replace("\\r", "\n")
                    ?.trim()
                    ?: "No description available.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LogoSquare(team: Team, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(170.dp)
            .shadow(elevation = 18.dp, shape = RoundedCornerShape(14.dp), clip = false),
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFFF8D7C7)
    ) {
        Box(contentAlignment = Alignment.Center) {
            TeamBadge(
                badgeUrl = team.badge,
                teamName = team.name,
                size = 84.dp
            )
        }
    }
}

@Composable
private fun FollowPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentHeight()
            .width(260.dp)
            .shadow(elevation = 20.dp, shape = RoundedCornerShape(999.dp), clip = false),
        shape = RoundedCornerShape(999.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 2.dp),
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
        letterSpacing = MaterialTheme.typography.titleLarge.letterSpacing
    )
}

@Composable
private fun OverviewCard(team: Team, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OverviewRow(
                label = "STADIUM",
                title = team.stadium ?: "-",
                subtitle = team.stadiumCapacity?.let { "Capacity: $it" },
                iconBg = Color(0xFFE7E3FF),
                iconTint = MaterialTheme.colorScheme.primary,
                icon = Icons.Outlined.Stadium
            )

            OverviewRow(
                label = "LOCATION",
                title = team.stadiumLocation ?: (team.country ?: "-"),
                subtitle = null,
                iconBg = Color(0xFFE7F6EC),
                iconTint = Color(0xFF22C55E),
                icon = Icons.Outlined.LocationOn
            )
        }
    }
}

@Composable
private fun OverviewRow(
    label: String,
    title: String,
    subtitle: String?,
    iconBg: Color,
    iconTint: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = CircleShape,
            color = iconBg,
            modifier = Modifier.size(30.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.70f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamDetailPixelClosePreview() {
    SportsAppTheme {
        TeamDetailBody(
            team = Team(
                id = "1",
                name = "Liverpool FC",
                shortName = "LIV",
                sport = "Soccer",
                league = "Premier League",
                country = "England",
                stadium = "Anfield",
                stadiumLocation = "Liverpool, Merseyside",
                stadiumCapacity = "61,276",
                badge = null,
                jersey = null,
                description = "Liverpool Football Club is a professional football club...\r\n\r\nMore text here.",
                formedYear = "1892",
                website = null,
                facebook = null,
                twitter = null,
                instagram = null,
                youtube = null
            ),
        )
    }
}

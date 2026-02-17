package com.sportsapp.feature.leagues

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SportsBaseball
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.SportsCricket
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.designsystem.component.ErrorState
import com.sportsapp.core.designsystem.component.LoadingIndicator
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.feature.leagues.components.LeagueDropdown
import com.sportsapp.feature.leagues.components.TeamGridItem
import com.sportsapp.core.designsystem.component.OfflineZeroState
import com.sportsapp.core.designsystem.component.SelectLeagueZeroState
import com.sportsapp.core.designsystem.component.SelectSportZeroState

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LeaguesScreen(
    onTeamClick: (String, String) -> Unit,
    viewModel: LeaguesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LeaguesContent(
        uiState = uiState,
        onSportSelected = viewModel::onSportSelected,
        onLeagueSelected = viewModel::onLeagueSelected,
        onLoadMore = viewModel::loadMore,
        onTeamClick = onTeamClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaguesContent(
    uiState: LeaguesUiState,
    onSportSelected: (String) -> Unit,
    onLeagueSelected: (String) -> Unit,
    onLoadMore: () -> Unit,
    onTeamClick: (String, String) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Leagues",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            SportsPillsRow(
                sports = Constants.Sports.ALL_SPORTS,
                selectedSport = uiState.selectedSport,
                onSportSelected = onSportSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (uiState.showLeagueDropdown) {
                LeagueDropdown(
                    leagues = uiState.availableLeagues,
                    selectedLeague = uiState.selectedLeague,
                    onLeagueSelected = onLeagueSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    screenEdgePadding = 16.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (uiState.showTeams) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ALL TEAMS",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )

                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "${uiState.displayedTeams.size} TEAMS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.showZeroState -> {
                        if (uiState.selectedSport != null) {
                            SelectLeagueZeroState()
                        } else {
                            SelectSportZeroState()
                        }
                    }

                    uiState.isLoadingTeams -> LoadingIndicator()

                    uiState.errorMessage != null -> {
                        if (uiState.errorMessage == Constants.ErrorMessages.NETWORK_ERROR) {
                            OfflineZeroState(
                                onTryAgain = {
                                    uiState.selectedLeague?.let { onLeagueSelected(it) }
                                }
                            )
                        } else {
                            ErrorState(
                                title = uiState.errorTitle ?: "Failed to load data",
                                message = uiState.errorMessage
                                    ?: "Something went wrong.\nPlease try again.",
                                actionText = uiState.errorAction ?: "Retry",
                                onRetry = {
                                    uiState.selectedLeague?.let { onLeagueSelected(it) }
                                }
                            )
                        }
                    }

                    uiState.showTeams -> TeamsGrid(
                        teams = uiState.displayedTeams,
                        hasMore = uiState.hasMoreTeams,
                        isLoadingMore = uiState.isLoadingMore,
                        onLoadMore = onLoadMore,
                        onTeamClick = onTeamClick
                    )

                    uiState.showEmptyTeams -> EmptyTeamsState()
                }
            }
        }
    }
}

@Composable
private fun SportsPillsRow(
    sports: List<String>,
    selectedSport: String?,
    onSportSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(sports) { sport ->
            SportPill(
                label = sport,
                selected = sport == selectedSport,
                onClick = { onSportSelected(sport) }
            )
        }
    }
}

@Composable
private fun SportPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container =
        if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)

    val content =
        if (selected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        color = container,
        contentColor = content,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(40.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = when (label) {
                    "Soccer" -> Icons.Outlined.SportsSoccer
                    "Basketball" -> Icons.Outlined.SportsBasketball
                    "Baseball" -> Icons.Outlined.SportsBaseball
                    "Cricket" -> Icons.Outlined.SportsCricket
                    else -> {
                        throw IllegalArgumentException("Invalid sport: $label")
                    }
                },
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ZeroState(selectedSport: String? = null, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (selectedSport != null) "Select a League" else "Select a Sport",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (selectedSport != null)
                "Choose a league from the dropdown above to view teams"
            else
                "Choose a sport from the chips above to view leagues and teams",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyTeamsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "No Teams Found", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "No teams found for this league",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TeamsGrid(
    teams: List<Team>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onTeamClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    val shouldLoadMore = remember(hasMore, isLoadingMore, teams.size) {
        derivedStateOf {
            if (!hasMore || isLoadingMore) return@derivedStateOf false
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= (teams.size - 4)
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) onLoadMore()
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(items = teams, key = { it.id }) { team ->
            TeamGridItem(
                team = team,
                onClick = { onTeamClick(team.id, team.name) }
            )
        }
        if (isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Teams Grid")
@Composable
private fun LeaguesScreenTeamsPreview() {
    SportsAppTheme {
        LeaguesContent(
            uiState = LeaguesUiState(
                selectedSport = "Soccer",
                selectedLeague = "Premier League",
                displayedTeams = List(6) { index ->
                    Team(
                        id = "$index",
                        name = listOf(
                            "Man City", "Liverpool", "Southampton",
                            "Leicester", "Nottm Forest", "Crystal Palace"
                        )[index],
                        shortName = "T$index",
                        sport = "Soccer",
                        league = "Premier League",
                        country = "England",
                        stadium = "Stadium",
                        stadiumLocation = "City",
                        stadiumCapacity = "50000",
                        badgeUrl = null,
                        description = null,
                        formedYear = null
                    )
                },
                hasMoreTeams = true
            ),
            onSportSelected = {},
            onLeagueSelected = {},
            onLoadMore = {},
            onTeamClick = { _, _ -> }
        )
    }
}

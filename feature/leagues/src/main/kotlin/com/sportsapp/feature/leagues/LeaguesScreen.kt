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
import androidx.compose.material.icons.outlined.Sports
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
import androidx.compose.ui.zIndex
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

            val selectedSport = (uiState as? LeaguesUiState.SportSelected)?.sport

            SportsPillsRow(
                sports = Constants.Sports.ALL_SPORTS,
                selectedSport = selectedSport,
                onSportSelected = onSportSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(14.dp))

            val teamsCount = (uiState as? LeaguesUiState.SportSelected)
                ?.teams
                ?.let { teamsState ->
                    when (teamsState) {
                        is TeamsState.Content -> teamsState.shown.size
                        else -> null
                    }
                }

            if (teamsCount != null) {
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
                            text = "$teamsCount TEAMS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            Column(modifier = Modifier.fillMaxSize()) {

                when (uiState) {
                    LeaguesUiState.Idle -> {
                        SelectSportZeroState()
                    }

                    is LeaguesUiState.SportSelected -> {
                        val selectedSport = uiState.sport

                        when (val leagues = uiState.leagues) {
                            LeagueListState.Hidden -> Unit
                            LeagueListState.Loading -> LoadingIndicator()

                            is LeagueListState.Error -> ErrorState(
                                title = leagues.title,
                                message = leagues.message,
                                actionText = leagues.action ?: "Retry",
                                onRetry = { onSportSelected(selectedSport) }
                            )

                            is LeagueListState.Ready -> {
                                if (leagues.items.isNotEmpty()) {
                                    LeagueDropdown(
                                        leagues = leagues.items,
                                        selectedLeague =
                                            (uiState.teams as? TeamsState.Content)?.selectedLeague
                                                ?: (uiState.teams as? TeamsState.Empty)?.selectedLeague
                                                ?: (uiState.teams as? TeamsState.Error)?.selectedLeague,
                                        onLeagueSelected = onLeagueSelected,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .zIndex(2f), // keep it above list
                                        screenEdgePadding = 16.dp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .zIndex(0f)
                        ) {
                            when (val teams = uiState.teams) {
                                TeamsState.NotSelected -> SelectLeagueZeroState()
                                TeamsState.Loading -> LoadingIndicator()

                                is TeamsState.Error -> ErrorState(
                                    title = teams.title,
                                    message = teams.message,
                                    actionText = teams.action ?: "Retry",
                                    onRetry = { teams.selectedLeague?.let(onLeagueSelected) }
                                )

                                is TeamsState.Empty -> EmptyTeamsState()

                                is TeamsState.Content -> TeamsGrid(
                                    teams = teams.shown,
                                    hasMore = teams.hasMore,
                                    isLoadingMore = teams.isLoadingMore,
                                    onLoadMore = onLoadMore,
                                    onTeamClick = onTeamClick,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
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
                        Icons.Outlined.Sports
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
    val league = "Premier League"
    val teams = List(6) { index ->
        Team(
            id = "$index",
            name = listOf(
                "Man City", "Liverpool", "Southampton",
                "Leicester", "Nottm Forest", "Crystal Palace"
            )[index],
            shortName = "T$index",
            sport = "Soccer",
            league = league,
            country = "England",
            stadium = "Stadium",
            stadiumLocation = "City",
            stadiumCapacity = "50000",
            badgeUrl = null,
            description = null,
            formedYear = null
        )
    }

    SportsAppTheme {
        LeaguesContent(
            uiState = LeaguesUiState.SportSelected(
                sport = Constants.Sports.SOCCER,
                leagues = LeagueListState.Ready(items = listOf("Premier League", "La Liga")),
                teams = TeamsState.Content(
                    selectedLeague = league,
                    all = teams,
                    shown = teams,
                    hasMore = true,
                    isLoadingMore = false
                )
            ),
            onSportSelected = {},
            onLeagueSelected = {},
            onLoadMore = {},
            onTeamClick = { _, _ -> }
        )
    }
}


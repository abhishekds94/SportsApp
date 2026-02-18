package com.sportsapp.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.designsystem.component.ErrorState
import com.sportsapp.core.designsystem.component.LoadingIndicator
import com.sportsapp.core.designsystem.component.OfflineZeroState
import com.sportsapp.core.designsystem.component.SearchIntroZeroState
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.feature.search.components.SearchBar
import com.sportsapp.feature.search.components.TeamResultItem

@Composable
fun SearchScreen(
    onTeamClick: (String, String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    SearchContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onTeamClick = onTeamClick
    )
}

@Composable
private fun SearchContent(
    uiState: SearchUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTeamClick: (String, String) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 8.dp)
                    .background(MaterialTheme.colorScheme.surface)
            )

            when (uiState) {
                SearchUiState.Idle -> {
                    SearchIntroZeroState()
                }

                SearchUiState.Loading -> {
                    LoadingIndicator()
                }

                is SearchUiState.Success -> {
                    SearchResults(uiState.teams, onTeamClick)
                }

                is SearchUiState.ZeroState -> {
                    // Use the search intro illustration but your custom title/message
                    com.sportsapp.core.designsystem.component.IllustratedZeroState(
                        illustrationRes = com.sportsapp.core.designsystem.R.drawable.zs_search,
                        title = uiState.title,
                        subtitle = uiState.message
                    )
                }

                is SearchUiState.Error -> {
                    if (uiState.message == Constants.ErrorMessages.NETWORK_ERROR) {
                        OfflineZeroState(onTryAgain = { onSearchQueryChange(searchQuery) })
                    } else {
                        ErrorState(
                            title = uiState.title,
                            message = uiState.message,
                            actionText = uiState.actionText ?: "Retry",
                            onRetry = { onSearchQueryChange(searchQuery) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    teams: List<Team>,
    onTeamClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(teams, key = { it.id }) { team ->
            TeamResultItem(
                team = team,
                onClick = { onTeamClick(team.id, team.name) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SportsAppTheme {
        SearchContent(
            uiState = SearchUiState.Idle,
            searchQuery = "",
            onSearchQueryChange = {},
            onTeamClick = { _, _ -> }
        )
    }
}

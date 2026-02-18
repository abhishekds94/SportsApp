package com.sportsapp.feature.leagues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.util.PagingController
import com.sportsapp.domain.leagues.usecase.GetAllLeaguesUseCase
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.GetTeamsByLeagueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase,
    private val getAllLeaguesUseCase: GetAllLeaguesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LeaguesUiState>(LeaguesUiState.Idle)
    val uiState: StateFlow<LeaguesUiState> = _uiState.asStateFlow()

    private var teamsJob: Job? = null
    private var leaguesJob: Job? = null

    private val paging = PagingController<Team>(initialSize = 8, pageSize = 8)

    fun onSportSelected(sport: String) {
        // toggle off behavior: selecting same sport clears
        val current = _uiState.value
        if (current is LeaguesUiState.SportSelected && current.sport == sport) {
            teamsJob?.cancel()
            leaguesJob?.cancel()
            _uiState.value = LeaguesUiState.Idle
            return
        }

        teamsJob?.cancel()
        paging.reset(emptyList())

        if (sport == Constants.Sports.SOCCER) {
            // Soccer: leagues come from API
            _uiState.value = LeaguesUiState.SportSelected(
                sport = sport,
                leagues = LeagueListState.Loading,
                teams = TeamsState.NotSelected
            )
            loadSoccerLeagues()
        } else {
            // Other sports: keep constants as-is
            val leagues = Constants.Sports.SPORT_LEAGUES[sport].orEmpty()
            _uiState.value = LeaguesUiState.SportSelected(
                sport = sport,
                leagues = LeagueListState.Ready(leagues),
                teams = TeamsState.NotSelected
            )
        }
    }

    private fun loadSoccerLeagues() {
        leaguesJob?.cancel()
        leaguesJob = viewModelScope.launch {
            getAllLeaguesUseCase().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        // API returns all sports; filter to Soccer only
                        val soccerLeagueNames = result.data
                            .asSequence()
                            .filter { it.sport.equals(Constants.Sports.SOCCER, ignoreCase = true) }
                            .map { it.name }
                            .distinct()
                            .sorted()
                            .toList()

                        _uiState.update { state ->
                            val s = state as? LeaguesUiState.SportSelected ?: return@update state
                            s.copy(leagues = LeagueListState.Ready(soccerLeagueNames))
                        }
                    }

                    is DomainResult.Error -> {
                        val ui = ErrorMapper.toUiMessage(ErrorMapper.toAppError(result.throwable))
                        _uiState.update { state ->
                            val s = state as? LeaguesUiState.SportSelected ?: return@update state
                            s.copy(leagues = LeagueListState.Error(ui.title, ui.message, ui.action))
                        }
                    }
                }
            }
        }
    }

    fun onLeagueSelected(leagueName: String) {
        val state = _uiState.value as? LeaguesUiState.SportSelected ?: return

        teamsJob?.cancel()
        paging.reset(emptyList())

        _uiState.value = state.copy(
            teams = TeamsState.Loading
        )

        loadTeamsForLeague(leagueName)
    }

    fun loadMore() {
        val state = _uiState.value as? LeaguesUiState.SportSelected ?: return
        val teamsState = state.teams as? TeamsState.Content ?: return
        if (teamsState.isLoadingMore || !teamsState.hasMore) return

        val page = paging.loadMore()

        _uiState.value = state.copy(
            teams = teamsState.copy(
                shown = page.shown,
                hasMore = page.hasMore,
                isLoadingMore = false
            )
        )
    }

    private fun loadTeamsForLeague(leagueName: String) {
        teamsJob = viewModelScope.launch {
            getTeamsByLeagueUseCase(leagueName).collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val teams = result.data
                        if (teams.isEmpty()) {
                            _uiState.update { st ->
                                val s = st as? LeaguesUiState.SportSelected ?: return@update st
                                s.copy(
                                    teams = TeamsState.Empty(
                                        selectedLeague = leagueName,
                                        title = "No teams",
                                        message = "No teams found for this league."
                                    )
                                )
                            }
                        } else {
                            val page = paging.reset(teams)
                            _uiState.update { st ->
                                val s = st as? LeaguesUiState.SportSelected ?: return@update st
                                s.copy(
                                    teams = TeamsState.Content(
                                        selectedLeague = leagueName,
                                        all = page.all,
                                        shown = page.shown,
                                        hasMore = page.hasMore,
                                        isLoadingMore = false
                                    )
                                )
                            }
                        }
                    }

                    is DomainResult.Error -> {
                        val ui = ErrorMapper.toUiMessage(ErrorMapper.toAppError(result.throwable))
                        _uiState.update { st ->
                            val s = st as? LeaguesUiState.SportSelected ?: return@update st
                            s.copy(
                                teams = TeamsState.Error(
                                    selectedLeague = leagueName,
                                    title = ui.title,
                                    message = ui.message,
                                    action = ui.action
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

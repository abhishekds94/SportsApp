package com.sportsapp.feature.leagues

import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.util.Constants
import com.sportsapp.domain.leagues.model.League
import com.sportsapp.domain.leagues.usecase.GetAllLeaguesUseCase
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.GetTeamsByLeagueUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LeaguesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTeamsByLeagueUseCase: GetTeamsByLeagueUseCase = mockk()
    private val getAllLeagueUseCase: GetAllLeaguesUseCase = mockk()

    private fun stubSoccerLeaguesSuccess() {
        val leagues = listOf(
            League(id = "1", name = "Premier League", sport = "Soccer", alternateName = null),
            League(id = "2", name = "La Liga", sport = "Soccer", alternateName = null),
            League(id = "3", name = "NBA", sport = "Basketball", alternateName = null)
        )

        every { getAllLeagueUseCase.invoke() } returns flowOf(DomainResult.Success(leagues))
    }


    @Test
    fun `onSportSelected for non-soccer uses constants leagues`() = runTest {
        val vm = LeaguesViewModel(getTeamsByLeagueUseCase, getAllLeagueUseCase)

        vm.onSportSelected(Constants.Sports.BASKETBALL)

        val state = vm.uiState.value as LeaguesUiState.SportSelected
        val leagues = state.leagues as LeagueListState.Ready

        assertEquals(Constants.Sports.BASKETBALL, state.sport)
        assertTrue(leagues.items.isNotEmpty())
    }


    @Test
    fun `onLeagueSelected loads teams and populates initial page`() = runTest {
        stubSoccerLeaguesSuccess()

        val vm = LeaguesViewModel(getTeamsByLeagueUseCase, getAllLeagueUseCase)
        vm.onSportSelected(Constants.Sports.SOCCER)

        val league = "Premier League"
        val teams = (1..20).map {
            Team(
                id = "$it",
                name = "Team $it",
                sport = "Soccer",
                league = league,
                badgeUrl = null,
                stadium = null,
                description = null
            )
        }

        every { getTeamsByLeagueUseCase.invoke(league) } returns flowOf(DomainResult.Success(teams))

        vm.onLeagueSelected(league)

        vm.uiState.test {
            while (true) {
                val state = awaitItem()
                val selected = state as? LeaguesUiState.SportSelected ?: continue
                val teamsState = selected.teams

                if (teamsState is TeamsState.Content) {
                    assertEquals(league, teamsState.selectedLeague)
                    assertEquals(teams.size, teamsState.all.size)
                    assertEquals(8, teamsState.shown.size)
                    assertTrue(teamsState.hasMore)
                    cancelAndIgnoreRemainingEvents()
                    return@test
                }
            }
        }
    }



    @Test
    fun `loadMore appends next page`() = runTest {
        stubSoccerLeaguesSuccess()

        val vm = LeaguesViewModel(getTeamsByLeagueUseCase, getAllLeagueUseCase)
        vm.onSportSelected(Constants.Sports.SOCCER)

        val league = "Premier League"
        val teams = (1..20).map {
            Team(
                id = "$it",
                name = "Team $it",
                sport = "Soccer",
                league = league,
                badgeUrl = null,
                stadium = null,
                description = null
            )
        }

        every { getTeamsByLeagueUseCase.invoke(league) } returns flowOf(DomainResult.Success(teams))

        vm.onLeagueSelected(league)

        vm.uiState.test {
            // Wait until content appears
            while (true) {
                val state = awaitItem()
                val selected = state as? LeaguesUiState.SportSelected ?: continue
                val content = selected.teams as? TeamsState.Content ?: continue

                assertEquals(8, content.shown.size)

                vm.loadMore()

                // Next emission should show 16
                while (true) {
                    val s2 = awaitItem()
                    val sel2 = s2 as? LeaguesUiState.SportSelected ?: continue
                    val content2 = sel2.teams as? TeamsState.Content ?: continue

                    if (content2.shown.size == 16) {
                        assertTrue(content2.hasMore)
                        cancelAndIgnoreRemainingEvents()
                        return@test
                    }
                }
            }
        }
    }



    @Test
    fun `league load error sets error state`() = runTest {
        stubSoccerLeaguesSuccess()

        val vm = LeaguesViewModel(getTeamsByLeagueUseCase, getAllLeagueUseCase)
        vm.onSportSelected(Constants.Sports.SOCCER)

        val league = "Premier League"
        every { getTeamsByLeagueUseCase.invoke(league) } returns flowOf(
            DomainResult.Error(RuntimeException("fail"))
        )

        vm.onLeagueSelected(league)

        vm.uiState.test {
            while (true) {
                val state = awaitItem()
                val selected = state as? LeaguesUiState.SportSelected ?: continue
                val teamsState = selected.teams

                if (teamsState is TeamsState.Error) {
                    assertEquals(league, teamsState.selectedLeague)
                    assertTrue(teamsState.title.isNotBlank())
                    assertTrue(teamsState.message.isNotBlank())
                    cancelAndIgnoreRemainingEvents()
                    return@test
                }
            }
        }
    }

    @Test
    fun `soccer selection loads leagues from api and filters soccer`() = runTest {
        val leagues = listOf(
            League(id = "1", name = "Premier League", sport = "Soccer", alternateName = null),
            League(id = "2", name = "NBA", sport = "Basketball", alternateName = null)
        )
        every { getAllLeagueUseCase.invoke() } returns flowOf(DomainResult.Success(leagues))

        val vm = LeaguesViewModel(getTeamsByLeagueUseCase, getAllLeagueUseCase)

        vm.onSportSelected(Constants.Sports.SOCCER)

        val state = vm.uiState.value as LeaguesUiState.SportSelected
        val ready = state.leagues as LeagueListState.Ready

        assertTrue(ready.items.contains("Premier League"))
        assertFalse(ready.items.contains("NBA"))
    }


}

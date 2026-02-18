package com.sportsapp.feature.leagues

import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.util.Constants
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

    @Test
    fun `onSportSelected sets available leagues and clears selected league`() = runTest {
        val vm = LeaguesViewModel(getTeamsByLeagueUseCase)

        val sport = Constants.Sports.SPORT_LEAGUES.keys.first()
        vm.onSportSelected(sport)

        val state = vm.uiState.value
        assertEquals(sport, state.selectedSport)
        assertTrue(state.availableLeagues.isNotEmpty())
        assertEquals(null, state.selectedLeague)
    }

    @Test
    fun `onLeagueSelected loads teams and populates initial page`() = runTest {
        val vm = LeaguesViewModel(getTeamsByLeagueUseCase)

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
            // first emissions include loading
            var state = awaitItem()
            while (state.isLoadingTeams) state = awaitItem()

            assertFalse(state.isLoadingTeams)
            assertEquals(league, state.selectedLeague)
            assertEquals(teams.size, state.allTeams.size)

            // initial page size is 8
            assertEquals(8, state.displayedTeams.size)
            assertTrue(state.hasMoreTeams)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMore appends next page`() = runTest {
        val vm = LeaguesViewModel(getTeamsByLeagueUseCase)

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

        // wait load finish
        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoadingTeams) state = awaitItem()

            assertEquals(8, state.displayedTeams.size)

            vm.loadMore()

            state = awaitItem()
            while (state.isLoadingMore) state = awaitItem()

            // after load more, should be 16 shown (8 + 8)
            assertEquals(16, state.displayedTeams.size)
            assertTrue(state.hasMoreTeams)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `league load error sets error state`() = runTest {
        val vm = LeaguesViewModel(getTeamsByLeagueUseCase)

        val league = "Premier League"
        every { getTeamsByLeagueUseCase.invoke(league) } returns flowOf(DomainResult.Error(RuntimeException("fail")))

        vm.onLeagueSelected(league)

        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoadingTeams) state = awaitItem()

            assertFalse(state.isLoadingTeams)
            assertTrue(state.errorTitle != null)
            assertTrue(state.errorMessage != null)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

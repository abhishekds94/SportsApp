package com.sportsapp.feature.search

import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.ObserveFavoriteTeamIdsUseCase
import com.sportsapp.domain.teams.usecase.SearchTeamsUseCase
import com.sportsapp.domain.teams.usecase.UnfollowTeamUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val searchTeamsUseCase: SearchTeamsUseCase = mockk()
    private val observeFavoriteTeamIdsUseCase: ObserveFavoriteTeamIdsUseCase = mockk()
    private val unfollowTeamUseCase: UnfollowTeamUseCase = mockk(relaxed = true)

    private fun createVm(): SearchViewModel {
        every { observeFavoriteTeamIdsUseCase.invoke() } returns flowOf(emptySet<String>())

        return SearchViewModel(
            searchTeamsUseCase = searchTeamsUseCase,
            observeFavoriteTeamIdsUseCase = observeFavoriteTeamIdsUseCase,
            unfollowTeamUseCase = unfollowTeamUseCase
        )
    }

    @Test
    fun `blank query sets Idle`() = runTest {
        val vm = createVm()

        vm.onSearchQueryChange("")
        assertTrue(vm.uiState.value is SearchUiState.Idle)
    }

    @Test
    fun `invalid query sets Error immediately`() = runTest {
        val vm = createVm()

        vm.onSearchQueryChange("a") // too short

        assertTrue(vm.uiState.value is SearchUiState.Error)
        verify(exactly = 0) { searchTeamsUseCase.invoke(any()) }
    }

    @Test
    fun `valid query triggers debounce and emits Success`() = runTest {
        val vm = createVm()

        val teams = listOf(
            Team(
                id = "1",
                name = "Arsenal",
                sport = "Soccer",
                league = "Premier League",
                badgeUrl = null,
                stadium = null,
                description = null
            )
        )

        every {
            searchTeamsUseCase.invoke(match { it.trim().lowercase() == "arsenal" })
        } returns flowOf(DomainResult.Success(teams))

        vm.uiState.test {
            // initial
            assertTrue(awaitItem() is SearchUiState.Idle)

            vm.onSearchQueryChange("Arsenal")

            // debounce in VM (~300ms)
            advanceTimeBy(350)
            advanceUntilIdle()

            // usually: Loading -> Success
            var state = awaitItem()
            while (state is SearchUiState.Loading) {
                state = awaitItem()
            }

            assertTrue(state is SearchUiState.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `valid query emits ZeroState when results empty`() = runTest {
        val vm = createVm()

        every {
            searchTeamsUseCase.invoke(match { it.trim().lowercase() == "arsenal" })
        } returns flowOf(DomainResult.Success(emptyList()))

        vm.uiState.test {
            assertTrue(awaitItem() is SearchUiState.Idle)

            vm.onSearchQueryChange("arsenal")

            advanceTimeBy(350)
            advanceUntilIdle()

            var state = awaitItem()
            while (state is SearchUiState.Loading) state = awaitItem()

            assertTrue(state is SearchUiState.ZeroState)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

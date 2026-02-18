package com.sportsapp.feature.search

import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.SearchTeamsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val searchTeamsUseCase: SearchTeamsUseCase = mockk()

    @Test
    fun `blank query sets Idle`() = runTest {
        val vm = SearchViewModel(searchTeamsUseCase)

        vm.onSearchQueryChange("")
        assertTrue(vm.uiState.value is SearchUiState.Idle)
    }

    @Test
    fun `invalid query sets Error immediately`() = runTest {
        val vm = SearchViewModel(searchTeamsUseCase)

        vm.onSearchQueryChange("a") // example: too short

        assertTrue(vm.uiState.value is SearchUiState.Error)
    }

    @Test
    fun `valid query triggers debounce and emits Success`() = runTest {
        val vm = SearchViewModel(searchTeamsUseCase)

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

        every { searchTeamsUseCase.invoke("arsenal") } returns flowOf(DomainResult.Success(teams))

        vm.uiState.test {
            // initial
            assertTrue(awaitItem() is SearchUiState.Idle)

            vm.onSearchQueryChange("arsenal")

            // debounce is 300ms in VM
            advanceTimeBy(350)

            // eventually should emit Success
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
        val vm = SearchViewModel(searchTeamsUseCase)

        every { searchTeamsUseCase.invoke("arsenal") } returns flowOf(DomainResult.Success(emptyList()))

        vm.uiState.test {
            assertTrue(awaitItem() is SearchUiState.Idle)

            vm.onSearchQueryChange("arsenal")
            advanceTimeBy(350)

            var state = awaitItem()
            while (state is SearchUiState.Loading) state = awaitItem()

            assertTrue(state is SearchUiState.ZeroState)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

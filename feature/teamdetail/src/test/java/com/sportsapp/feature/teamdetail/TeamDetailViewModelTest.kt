package com.sportsapp.feature.teamdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.GetTeamByNameUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TeamDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getTeamByNameUseCase: GetTeamByNameUseCase = mockk()

    @Test
    fun `init loads team successfully`() = runTest {
        val teamName = "Arsenal"
        val team = Team(
            id = "1",
            name = "Arsenal",
            sport = "Soccer",
            league = "Premier League",
            badgeUrl = null,
            stadium = null,
            description = null
        )

        every { getTeamByNameUseCase.invoke(teamName) } returns flowOf(DomainResult.Success(team))

        val vm = TeamDetailViewModel(
            getTeamByNameUseCase = getTeamByNameUseCase,
            savedStateHandle = SavedStateHandle(mapOf("teamName" to teamName))
        )

        vm.uiState.test {
            val first = awaitItem()
            // initial state is default TeamDetailUiState, then loading update happens
            // depending on dispatcher scheduling, you may see loading first:
            // We keep it robust by consuming until non-loading.
            var state = first
            while (state.isLoadingTeam) {
                state = awaitItem()
            }

            assertFalse(state.isLoadingTeam)
            assertEquals("Arsenal", state.team?.name)
            assertNull(state.errorMessage)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `team not found emits empty state`() = runTest {
        val teamName = "DoesNotExist"

        every { getTeamByNameUseCase.invoke(teamName) } returns flowOf(DomainResult.Success(null))

        val vm = TeamDetailViewModel(
            getTeamByNameUseCase = getTeamByNameUseCase,
            savedStateHandle = SavedStateHandle(mapOf("teamName" to teamName))
        )

        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoadingTeam) {
                state = awaitItem()
            }

            assertFalse(state.isLoadingTeam)
            assertNull(state.team)
            assertTrue(state.errorTitle?.contains("Team not found") == true)
            assertTrue(state.errorMessage?.contains("Try searching again") == true)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error from use case emits error ui`() = runTest {
        val teamName = "Arsenal"
        val throwable = RuntimeException("Boom")

        every { getTeamByNameUseCase.invoke(teamName) } returns flowOf(DomainResult.Error(throwable))

        val vm = TeamDetailViewModel(
            getTeamByNameUseCase = getTeamByNameUseCase,
            savedStateHandle = SavedStateHandle(mapOf("teamName" to teamName))
        )

        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoadingTeam) {
                state = awaitItem()
            }

            assertFalse(state.isLoadingTeam)
            assertNull(state.team)
            assertTrue(state.errorTitle != null)
            assertTrue(state.errorMessage != null)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry triggers another load`() = runTest {
        val teamName = "Arsenal"
        val team = Team(
            id = "1",
            name = "Arsenal",
            sport = "Soccer",
            league = "Premier League",
            badgeUrl = null,
            stadium = null,
            description = null
        )

        // first error then success
        every { getTeamByNameUseCase.invoke(teamName) } returnsMany listOf(
            flowOf(DomainResult.Error(RuntimeException("fail"))),
            flowOf(DomainResult.Success(team))
        )

        val vm = TeamDetailViewModel(
            getTeamByNameUseCase = getTeamByNameUseCase,
            savedStateHandle = SavedStateHandle(mapOf("teamName" to teamName))
        )

        // wait for first load to finish (error)
        vm.uiState.test {
            var state = awaitItem()
            while (state.isLoadingTeam) state = awaitItem()
            assertNull(state.team)
            assertTrue(state.errorMessage != null)

            vm.retry()

            // after retry, should eventually be success
            state = awaitItem()
            while (state.isLoadingTeam) state = awaitItem()
            assertEquals("Arsenal", state.team?.name)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

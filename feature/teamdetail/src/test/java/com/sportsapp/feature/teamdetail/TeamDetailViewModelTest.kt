package com.sportsapp.feature.teamdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.FollowTeamUseCase
import com.sportsapp.domain.teams.usecase.GetTeamByNameUseCase
import com.sportsapp.domain.teams.usecase.ObserveIsTeamFollowedUseCase
import com.sportsapp.domain.teams.usecase.UnfollowTeamUseCase
import io.mockk.coEvery
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
    private val followTeamUseCase: FollowTeamUseCase = mockk(relaxed = true)
    private val observeIsTeamFollowedUseCase: ObserveIsTeamFollowedUseCase = mockk()
    private val unfollowTeamUseCase: UnfollowTeamUseCase = mockk(relaxed = true)

    private fun createVm(
        teamName: String,
        followedFlow: Boolean = false
    ): TeamDetailViewModel {
        every { observeIsTeamFollowedUseCase.invoke(any()) } returns flowOf(followedFlow)

        return TeamDetailViewModel(
            getTeamByNameUseCase = getTeamByNameUseCase,
            followTeamUseCase = followTeamUseCase,
            observeIsTeamFollowedUseCase = observeIsTeamFollowedUseCase,
            unfollowTeamUseCase = unfollowTeamUseCase,
            savedStateHandle = SavedStateHandle(mapOf("teamName" to teamName))
        )
    }

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

        val vm = createVm(teamName = teamName, followedFlow = false)

        vm.uiState.test {
            var state = awaitItem()
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

        val vm = createVm(teamName = teamName)

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

        val vm = createVm(teamName = teamName)

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

        val vm = createVm(teamName = teamName)

        vm.uiState.test {
            // first load (error)
            var state = awaitItem()
            while (state.isLoadingTeam) state = awaitItem()
            assertNull(state.team)
            assertTrue(state.errorMessage != null)

            // retry
            vm.retry()

            // second load (success)
            state = awaitItem()
            while (state.isLoadingTeam) state = awaitItem()
            assertEquals("Arsenal", state.team?.name)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

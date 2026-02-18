package com.sportsapp.feature.favorites

import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.ObserveFavoriteTeamsUseCase
import com.sportsapp.domain.teams.usecase.UnfollowTeamUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `favoriteTeams starts with emptyList when no favorites`() = runTest {
        val repo = FakeTeamsRepository(initialFavorites = emptyList())
        val observeUC = ObserveFavoriteTeamsUseCase(repo)
        val unfollowUC = UnfollowTeamUseCase(repo)

        val vm = FavoritesViewModel(
            observeFavoriteTeamsUseCase = observeUC,
            unfollowTeamUseCase = unfollowUC
        )

        // stateIn initial value is emptyList()
        assertEquals(emptyList<Team>(), vm.favoriteTeams.value)
    }

    @Test
    fun `onUnfollowToggle removes team from favorites`() = runTest {
        val team1 = Team(id = "t1", name = "Arsenal")
        val team2 = Team(id = "t2", name = "Chelsea")

        val repo = FakeTeamsRepository(initialFavorites = listOf(team1, team2))
        val observeUC = ObserveFavoriteTeamsUseCase(repo)
        val unfollowUC = UnfollowTeamUseCase(repo)

        val vm = FavoritesViewModel(
            observeFavoriteTeamsUseCase = observeUC,
            unfollowTeamUseCase = unfollowUC
        )

        val job = launch { vm.favoriteTeams.collect { /* no-op */ } }

        advanceUntilIdle()
        assertEquals(2, vm.favoriteTeams.value.size)

        vm.onUnfollowToggle("t1")
        advanceUntilIdle()

        assertEquals("t1", repo.lastUnfollowedTeamId)
        assertEquals(listOf(team2), vm.favoriteTeams.value)

        job.cancel()
    }

}

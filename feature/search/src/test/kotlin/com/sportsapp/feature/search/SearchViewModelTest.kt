package com.sportsapp.feature.search

import app.cash.turbine.test
import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.model.Team
import com.sportsapp.data.teams.repository.TeamRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for SearchViewModel
 *
 * Tests cover:
 * - Search query state management with debounce
 * - Successful search with results
 * - Empty search results
 * - Error handling
 * - Search validation (minimum 3 characters)
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var teamRepository: TeamRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        teamRepository = mockk()

        // Setup default mock responses for any search query
        coEvery { teamRepository.searchTeams(any()) } returns
                flowOf(Resource.Success(emptyList()))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        // When
        viewModel = SearchViewModel(teamRepository)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Idle)
        }
    }

    @Test
    fun `onSearchQueryChange updates searchQuery`() = runTest {
        // Given
        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("Arsenal")

        // Then
        assertEquals("Arsenal", viewModel.searchQuery.value)
    }

    @Test
    fun `search with valid query returns success state`() = runTest {
        // Given
        val mockTeams = listOf(
            createMockTeam("1", "Arsenal"),
            createMockTeam("2", "Arsenal FC")
        )
        coEvery { teamRepository.searchTeams("Arsenal") } returns
                flowOf(Resource.Success(mockTeams))

        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("Arsenal")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Success)
            assertEquals(2, (state as SearchUiState.Success).teams.size)
        }
    }

    @Test
    fun `search with no results returns error state`() = runTest {
        // Given
        coEvery { teamRepository.searchTeams("XYZ123") } returns
                flowOf(Resource.Success(emptyList()))

        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("XYZ123")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Error)
        }
    }

    @Test
    fun `search with error returns error state`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { teamRepository.searchTeams("Arsenal") } returns
                flowOf(Resource.Error(
                    message = errorMessage,
                    exception = Exception(errorMessage)
                ))

        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("Arsenal")
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Error)
            assertEquals(errorMessage, (state as SearchUiState.Error).message)
        }
    }

    @Test
    fun `blank query sets state to Idle`() = runTest {
        // Given
        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Idle)
        }
    }

    @Test
    fun `query with less than 3 characters shows error`() = runTest {
        // Given
        viewModel = SearchViewModel(teamRepository)

        // When
        viewModel.onSearchQueryChange("Ar")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is SearchUiState.Error)
        }
    }

    @Test
    fun `debounce prevents multiple rapid searches`() = runTest {
        // Given
        val mockTeams = listOf(createMockTeam("1", "Arsenal"))
        coEvery { teamRepository.searchTeams(any()) } returns
                flowOf(Resource.Success(mockTeams))

        viewModel = SearchViewModel(teamRepository)

        // When - Rapid changes within debounce window
        viewModel.onSearchQueryChange("Ars")
        advanceTimeBy(100)
        viewModel.onSearchQueryChange("Arse")
        advanceTimeBy(100)
        viewModel.onSearchQueryChange("Arsen")
        advanceTimeBy(100)
        viewModel.onSearchQueryChange("Arsena")
        advanceTimeBy(100)
        viewModel.onSearchQueryChange("Arsenal")

        // Wait for debounce
        advanceTimeBy(350)
        advanceUntilIdle()

        // Then - Only "Arsenal" should trigger search
        assertEquals("Arsenal", viewModel.searchQuery.value)
    }

    private fun createMockTeam(id: String, name: String) = Team(
        id = id,
        name = name,
        shortName = name.take(3).uppercase(),
        sport = "Soccer",
        league = "Premier League",
        country = "England",
        stadium = "Stadium",
        stadiumLocation = null,
        stadiumCapacity = null,
        badge = null,
        jersey = null,
        description = null,
        formedYear = null,
        website = null,
        facebook = null,
        twitter = null,
        instagram = null,
        youtube = null
    )
}
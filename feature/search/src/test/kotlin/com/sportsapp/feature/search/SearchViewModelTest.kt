package com.sportsapp.feature.search

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sportsapp.core.common.util.Resource
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.SearchTeamsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var searchTeamsUseCase: SearchTeamsUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        searchTeamsUseCase = mockk()
        viewModel = SearchViewModel(searchTeamsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when query changes, emits Loading then Success`() = runTest {
        val teams = listOf(Team(id = "1", name = "Team A"))
        coEvery { searchTeamsUseCase.invoke("Team") } returns flow {
            emit(Resource.Loading)
            emit(Resource.Success(teams))
        }

        viewModel.uiState.test {
            // initial
            assertThat(awaitItem()).isEqualTo(SearchUiState.Idle)

            viewModel.onSearchQueryChange("Team")
            dispatcher.scheduler.advanceUntilIdle()

            // We can see Loading then Success
            assertThat(awaitItem()).isEqualTo(SearchUiState.Loading)
            val success = awaitItem() as SearchUiState.Success
            assertThat(success.teams).isEqualTo(teams)
        }
    }
}

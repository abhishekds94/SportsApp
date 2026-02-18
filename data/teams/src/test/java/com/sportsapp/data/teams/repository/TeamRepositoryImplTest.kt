package com.sportsapp.data.teams.repository

import app.cash.turbine.test
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.network.model.TeamDto
import com.sportsapp.data.teams.source.remote.TeamRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class TeamRepositoryImplTest {

    private val remote: TeamRemoteDataSource = mockk()
    private val repo = TeamRepositoryImpl(remote)

    // ---- helpers ----
    private fun dto(
        id: String = "1",
        name: String = "Arsenal",
        sport: String? = "Soccer",
        league: String? = "Premier League"
    ): TeamDto {
        return TeamDto(
            id = id,
            name = name,
            sport = sport,
            league = league,
            badge = null,
            stadium = null,
            description = null
        )
    }

    // -------------------- searchTeamsByLeague --------------------

    @Test
    fun `searchTeamsByLeague emits Success with mapped teams`() = runTest {
        val league = "Premier League"
        val dtos = listOf(dto(id = "1", name = "Arsenal"), dto(id = "2", name = "Chelsea"))

        coEvery { remote.searchTeamsByLeague(league) } returns dtos

        repo.searchTeamsByLeague(league).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Success)

            val data = (item as DomainResult.Success).data
            assertEquals(2, data.size)
            assertEquals("Arsenal", data[0].name)

            awaitComplete()
        }
    }

    @Test
    fun `searchTeamsByLeague emits Error when remote throws`() = runTest {
        val league = "Premier League"
        val ex = RuntimeException("network fail")

        coEvery { remote.searchTeamsByLeague(league) } throws ex

        repo.searchTeamsByLeague(league).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Error)
            assertEquals(ex, (item as DomainResult.Error).throwable)
            awaitComplete()
        }
    }

    // -------------------- searchTeams --------------------

    @Test
    fun `searchTeams emits Success with mapped teams`() = runTest {
        val query = "arsenal"
        val dtos = listOf(dto(id = "1", name = "Arsenal"))

        coEvery { remote.searchTeams(query) } returns dtos

        repo.searchTeams(query).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Success)

            val data = (item as DomainResult.Success).data
            assertEquals(1, data.size)
            assertEquals("Arsenal", data[0].name)

            awaitComplete()
        }
    }

    @Test
    fun `searchTeams emits Error when remote throws`() = runTest {
        val query = "arsenal"
        val ex = IllegalStateException("boom")

        coEvery { remote.searchTeams(query) } throws ex

        repo.searchTeams(query).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Error)
            assertEquals(ex, (item as DomainResult.Error).throwable)
            awaitComplete()
        }
    }

    // -------------------- getTeamByName --------------------

    @Test
    fun `getTeamByName emits Success with mapped team`() = runTest {
        val teamName = "Arsenal"
        val teamDto = dto(id = "1", name = "Arsenal")

        coEvery { remote.getTeamByName(teamName) } returns teamDto

        repo.getTeamByName(teamName).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Success)

            val data = (item as DomainResult.Success).data
            assertTrue(data != null)
            assertEquals("Arsenal", data?.name)

            awaitComplete()
        }
    }

    @Test
    fun `getTeamByName emits Success with null when remote returns null`() = runTest {
        val teamName = "DoesNotExist"

        coEvery { remote.getTeamByName(teamName) } returns null

        repo.getTeamByName(teamName).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Success)

            val data = (item as DomainResult.Success).data
            assertEquals(null, data)

            awaitComplete()
        }
    }

    @Test
    fun `getTeamByName emits Error when remote throws`() = runTest {
        val teamName = "Arsenal"
        val ex = RuntimeException("timeout")

        coEvery { remote.getTeamByName(teamName) } throws ex

        repo.getTeamByName(teamName).test {
            val item = awaitItem()
            assertTrue(item is DomainResult.Error)
            assertEquals(ex, (item as DomainResult.Error).throwable)
            awaitComplete()
        }
    }
}

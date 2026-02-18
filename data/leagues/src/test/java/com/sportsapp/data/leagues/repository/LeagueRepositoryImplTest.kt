package com.sportsapp.data.leagues.repository

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.network.model.LeagueDto
import com.sportsapp.data.leagues.source.remote.LeagueRemoteDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LeagueRepositoryImplTest {

    @Test
    fun `getAllLeagues emits Success when remote returns leagues`() = runTest {
        val remote = mockk<LeagueRemoteDataSource>()
        coEvery { remote.getAllLeagues() } returns listOf(
            LeagueDto(id = "1", name = "English Premier League", sport = "Soccer"),
            LeagueDto(id = "2", name = "NBA", sport = "Basketball")
        )

        val repo = LeagueRepositoryImpl(remote)
        val result = repo.getAllLeagues().first()

        assertTrue(result is DomainResult.Success)
        val data = (result as DomainResult.Success).data
        assertEquals(2, data.size)
        assertEquals("English Premier League", data[0].name)
    }

    @Test
    fun `getAllLeagues emits Error when remote throws`() = runTest {
        val remote = mockk<LeagueRemoteDataSource>()
        coEvery { remote.getAllLeagues() } throws IOException("no internet")

        val repo = LeagueRepositoryImpl(remote)
        val result = repo.getAllLeagues().first()

        assertTrue(result is DomainResult.Error)
    }
}

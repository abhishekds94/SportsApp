package com.sportsapp.domain.leagues.usecase

import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.domain.leagues.model.League
import com.sportsapp.domain.leagues.repository.LeaguesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllLeaguesUseCase @Inject constructor(
    private val repo: LeaguesRepository
) {
    operator fun invoke(): Flow<DomainResult<List<League>>> = repo.getAllLeagues()
}

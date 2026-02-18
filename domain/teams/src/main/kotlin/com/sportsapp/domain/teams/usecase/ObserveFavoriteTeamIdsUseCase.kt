package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteTeamIdsUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    operator fun invoke(): Flow<Set<String>> = repo.observeFavoriteTeamIds()
}
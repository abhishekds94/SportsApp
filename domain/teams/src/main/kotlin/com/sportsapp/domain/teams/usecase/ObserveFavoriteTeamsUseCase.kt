package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteTeamsUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    operator fun invoke(): Flow<List<Team>> = repo.observeFavoriteTeams()
}

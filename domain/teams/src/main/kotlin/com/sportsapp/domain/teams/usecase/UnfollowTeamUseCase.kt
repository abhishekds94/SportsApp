package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class UnfollowTeamUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    suspend operator fun invoke(teamId: String) {
        repo.unfollowTeam(teamId)
    }
}

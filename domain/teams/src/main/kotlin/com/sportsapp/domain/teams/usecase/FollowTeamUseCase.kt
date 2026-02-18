package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class FollowTeamUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    suspend operator fun invoke(team: Team) = repo.followTeam(team)
}
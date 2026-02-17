
package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class GetTeamByNameUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    operator fun invoke(teamName: String) = repo.getTeamByName(teamName)
}

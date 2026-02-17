
package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class GetTeamsByLeagueUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    operator fun invoke(leagueName: String) = repo.searchTeamsByLeague(leagueName)
}

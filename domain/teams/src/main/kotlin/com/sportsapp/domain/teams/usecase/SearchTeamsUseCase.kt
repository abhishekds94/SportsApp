
package com.sportsapp.domain.teams.usecase

import com.sportsapp.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class SearchTeamsUseCase @Inject constructor(
    private val repo: TeamsRepository
) {
    operator fun invoke(query: String) = repo.searchTeams(query)
}

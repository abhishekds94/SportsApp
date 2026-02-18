package com.sportsapp.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.ObserveFavoriteTeamsUseCase
import com.sportsapp.domain.teams.usecase.UnfollowTeamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    observeFavoriteTeamsUseCase: ObserveFavoriteTeamsUseCase,
    private val unfollowTeamUseCase: UnfollowTeamUseCase
) : ViewModel() {

    val favoriteTeams: StateFlow<List<Team>> = observeFavoriteTeamsUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onUnfollowToggle(teamId: String) {
        viewModelScope.launch {
            unfollowTeamUseCase(teamId)
        }
    }
}

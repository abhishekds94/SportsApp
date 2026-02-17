package com.sportsapp.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.extensions.isValidSearchQuery
import com.sportsapp.core.common.result.DomainResult
import com.sportsapp.core.common.ui.LoadState
import com.sportsapp.core.common.ui.toLoadState
import com.sportsapp.core.common.util.Constants
import com.sportsapp.domain.teams.model.Team
import com.sportsapp.domain.teams.usecase.SearchTeamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchTeamsUseCase: SearchTeamsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        if (!query.isValidSearchQuery()) {
            _uiState.value = SearchUiState.Error(
                title = "Invalid search",
                message = Constants.ErrorMessages.EMPTY_SEARCH,
                actionText = null,
                throwable = null
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) return@collectLatest
                    if (!query.isValidSearchQuery()) return@collectLatest
                    search(query)
                }
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            searchTeamsUseCase(query).collectLatest { result: DomainResult<List<Team>> ->
                val state: LoadState<List<Team>> = result.toLoadState(
                    defaultErrorTitle = "Failed to search",
                    isEmpty = { it.isEmpty() },
                    emptyTitle = "No results",
                    emptyMessage = "Try a different keyword."
                )

                _uiState.value = when (state) {
                    LoadState.Idle -> SearchUiState.Idle
                    LoadState.Loading -> SearchUiState.Loading

                    is LoadState.Success -> SearchUiState.Success(state.data)

                    is LoadState.Empty -> SearchUiState.ZeroState(
                        title = state.ui.title,
                        message = state.ui.message
                    )

                    is LoadState.Error -> SearchUiState.Error(
                        title = state.ui.title,
                        message = state.ui.message,
                        actionText = state.ui.action,
                        throwable = state.throwable
                    )
                }
            }
        }
    }
}

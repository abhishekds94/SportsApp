package com.sportsapp.feature.search

import SearchUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.AppError
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.extensions.asResult
import com.sportsapp.core.common.extensions.isValidSearchQuery
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.util.Resource
import com.sportsapp.domain.teams.usecase.SearchTeamsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
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
            searchTeamsUseCase(query)
                .asResult()
                .collectLatest { res ->
                    when (res) {
                        is Resource.Loading -> {
                            _uiState.value = SearchUiState.Loading
                        }

                        is Resource.Success -> {
                            val list = res.data
                            _uiState.value =
                                if (list.isEmpty()) {
                                    SearchUiState.ZeroState(
                                        title = "No results",
                                        message = Constants.ErrorMessages.NO_RESULTS
                                    )
                                } else {
                                    SearchUiState.Success(list)
                                }
                        }

                        is Resource.Error -> {
                            val appError = res.throwable?.let(ErrorMapper::toAppError) ?: AppError.Unknown
                            val ui = ErrorMapper.toUiMessage(appError)

                            _uiState.value = SearchUiState.Error(
                                title = ui.title,
                                message = ui.message,
                                actionText = ui.action,
                                throwable = res.throwable
                            )
                        }
                    }
                }
        }
    }
}

package com.sportsapp.feature.search

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.error.UiErrorMessage
import com.sportsapp.core.common.extensions.asResult
import com.sportsapp.core.common.extensions.isValidSearchQuery
import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.teams.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val teamRepository: TeamRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        observeSearchQuery()
    }
    
    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .filter { it.isValidSearchQuery() }
                .collect { query ->
                    searchTeams(query)
                }
        }
    }
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
        } else if (!query.isValidSearchQuery()) {
            _uiState.value = SearchUiState.Error(Constants.ErrorMessages.EMPTY_SEARCH, Constants.ErrorMessages.EMPTY_SEARCH)
        }
    }
    
    private fun searchTeams(query: String) {
        viewModelScope.launch {
            teamRepository.searchTeams(query)
                .asResult()
                .collect { resource ->
                    _uiState.value = when (resource) {
                        is Resource.Loading -> SearchUiState.Loading
                        is Resource.Success -> {
                            if (resource.data.isEmpty()) {
                                SearchUiState.Error(
                                    Constants.ErrorMessages.NO_RESULTS,
                                    message = Constants.ErrorMessages.NO_RESULTS,
                                )
                            } else {
                                SearchUiState.Success(resource.data)
                            }
                        }
                        is Resource.Error -> SearchUiState.Error(resource.message ?: "", Constants.ErrorMessages.SEARCH_ERROR)
                    }
                }
        }
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun setError(t: Throwable?, fallbackMessage: String = "Something went wrong.\nPlease try again.") {
        val ui = t?.let { ErrorMapper.toUiMessage(ErrorMapper.toAppError(it)) }
            ?: UiErrorMessage(
                title = "Failed to load data",
                message = fallbackMessage,
                action = "Retry"
            )

        _uiState.value = SearchUiState.Error(
            title = ui.title,
            message = ui.message,
            actionText = ui.action ?: "Retry",
            throwable = t
        )
    }
}
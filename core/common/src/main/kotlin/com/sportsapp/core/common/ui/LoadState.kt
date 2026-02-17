package com.sportsapp.core.common.ui

sealed class LoadState<out T> {
    data object Idle : LoadState<Nothing>()
    data object Loading : LoadState<Nothing>()

    data class Success<T>(val data: T) : LoadState<T>()
    data class Empty(val ui: UiMessage) : LoadState<Nothing>()
    data class Error(val ui: UiMessage, val throwable: Throwable? = null) : LoadState<Nothing>()
}

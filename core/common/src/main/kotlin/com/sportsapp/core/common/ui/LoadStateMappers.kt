package com.sportsapp.core.common.ui

import com.sportsapp.core.common.error.ErrorMapper
import com.sportsapp.core.common.result.DomainResult

fun Throwable.toUiMessage(defaultTitle: String = "Something went wrong"): UiMessage {
    val appError = ErrorMapper.toAppError(this)
    val ui = ErrorMapper.toUiMessage(appError)
    return UiMessage(
        title = ui.title.ifBlank { defaultTitle },
        message = ui.message,
        action = ui.action
    )
}

fun <T> DomainResult<T>.toLoadState(
    defaultErrorTitle: String = "Something went wrong",
    isEmpty: (T) -> Boolean = { false },
    emptyTitle: String = "Nothing here",
    emptyMessage: String = "No data found"
): LoadState<T> {
    return when (this) {
        is DomainResult.Success -> {
            if (isEmpty(this.data)) {
                LoadState.Empty(UiMessage(title = emptyTitle, message = emptyMessage, action = null))
            } else {
                LoadState.Success(this.data)
            }
        }

        is DomainResult.Error -> {
            LoadState.Error(ui = this.throwable.toUiMessage(defaultErrorTitle), throwable = this.throwable)
        }
    }
}

package com.sportsapp.core.common.ui

data class UiMessage(
    val title: String,
    val message: String,
    val action: String? = "Retry"
)

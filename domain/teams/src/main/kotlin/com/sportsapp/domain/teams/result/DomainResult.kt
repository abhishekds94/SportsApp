package com.sportsapp.domain.teams.result

sealed class DomainResult<out T> {
    data class Success<T>(val data: T) : DomainResult<T>()
    data class Error(val throwable: Throwable) : DomainResult<Nothing>()
}

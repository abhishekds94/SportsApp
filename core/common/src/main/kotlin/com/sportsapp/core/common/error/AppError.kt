package com.sportsapp.core.common.error

sealed interface AppError {
    data object Network : AppError                 // no internet / DNS / etc
    data object Timeout : AppError                 // socket timeout
    data object Server : AppError                  // 5xx
    data class Http(val code: Int) : AppError      // 4xx other than 401/403/404 optionally
    data object Unauthorized : AppError            // 401
    data object Forbidden : AppError               // 403
    data object NotFound : AppError                // 404
    data object RateLimited : AppError             // 429
    data object Serialization : AppError           // JSON parse / unexpected response
    data object Unknown : AppError
}

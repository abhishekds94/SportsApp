package com.sportsapp.core.common.error

import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException

object ErrorMapper {

    fun toAppError(t: Throwable): AppError {
        val cause = unwrap(t)

        return when (cause) {
            is SocketTimeoutException -> AppError.Timeout
            is SSLException -> AppError.Network
            is IOException -> AppError.Network

            else -> {
                // Try to infer HTTP codes without depending on retrofit2
                val httpCode = extractHttpCode(cause)
                if (httpCode != null) {
                    when (httpCode) {
                        401 -> AppError.Unauthorized
                        403 -> AppError.Forbidden
                        404 -> AppError.NotFound
                        429 -> AppError.RateLimited
                        in 500..599 -> AppError.Server
                        else -> AppError.Http(httpCode)
                    }
                } else {
                    // Parsing / serialization-like issues (no dependency on kotlinx.serialization)
                    if (looksLikeParsingError(cause)) AppError.Serialization else AppError.Unknown
                }
            }
        }
    }

    fun toUiMessage(error: AppError): UiErrorMessage {
        return when (error) {
            AppError.Network -> UiErrorMessage(
                title = "You’re Offline",
                message = "Check your connection and try again.",
                action = "Try Again"
            )

            AppError.Timeout -> UiErrorMessage(
                title = "Request timed out",
                message = "That took longer than expected. Please try again.",
                action = "Retry"
            )

            AppError.Server -> UiErrorMessage(
                title = "Failed to load data",
                message = "Something went wrong on our end.\nPlease try again.",
                action = "Retry"
            )

            AppError.RateLimited -> UiErrorMessage(
                title = "Too many requests",
                message = "Please wait a moment and try again.",
                action = "Retry"
            )

            AppError.Unauthorized -> UiErrorMessage(
                title = "Session expired",
                message = "Please sign in again to continue.",
                action = "Sign in"
            )

            AppError.Forbidden -> UiErrorMessage(
                title = "Access denied",
                message = "You don’t have permission to view this.",
                action = null
            )

            AppError.NotFound -> UiErrorMessage(
                title = "Not found",
                message = "We couldn’t find what you’re looking for.",
                action = null
            )

            is AppError.Http -> UiErrorMessage(
                title = "Failed to load data",
                message = "Something went wrong.\nPlease try again.",
                action = "Retry"
            )

            AppError.Serialization -> UiErrorMessage(
                title = "Failed to load data",
                message = "We received an unexpected response.\nPlease try again.",
                action = "Retry"
            )

            AppError.Unknown -> UiErrorMessage(
                title = "Failed to load data",
                message = "Something went wrong.\nPlease try again.",
                action = "Retry"
            )
        }
    }

    private fun unwrap(t: Throwable): Throwable = t.cause ?: t

    /**
     * Attempts to extract an HTTP status code without depending on Retrofit types.
     * Supports common patterns:
     * - exception has a "code()" method
     * - exception has a "code" field / property
     * - message contains "HTTP 404" / "HTTP 500" etc
     */
    private fun extractHttpCode(t: Throwable): Int? {
        // 1) message parsing fallback
        parseHttpCodeFromMessage(t.message)?.let { return it }

        // 2) reflection: code() method
        runCatching {
            val m = t.javaClass.methods.firstOrNull { it.name == "code" && it.parameterTypes.isEmpty() }
            val v = m?.invoke(t)
            if (v is Int) return v
        }

        // 3) reflection: getCode() method
        runCatching {
            val m = t.javaClass.methods.firstOrNull { it.name == "getCode" && it.parameterTypes.isEmpty() }
            val v = m?.invoke(t)
            if (v is Int) return v
        }

        // 4) reflection: "code" field
        runCatching {
            val f = t.javaClass.declaredFields.firstOrNull { it.name == "code" }
            f?.isAccessible = true
            val v = f?.get(t)
            if (v is Int) return v
        }

        return null
    }

    private fun parseHttpCodeFromMessage(message: String?): Int? {
        if (message.isNullOrBlank()) return null
        // Look for "HTTP 404", "HTTP/1.1 404", "Status Code: 404"
        val regex = Regex("""\b(HTTP(?:/\d(?:\.\d)?)?\s*)?(\d{3})\b""")
        val match = regex.find(message) ?: return null
        val code = match.groupValues.getOrNull(2)?.toIntOrNull()
        return if (code != null && code in 100..599) code else null
    }

    private fun looksLikeParsingError(t: Throwable): Boolean {
        val name = t.javaClass.name.lowercase()
        val msg = (t.message ?: "").lowercase()

        return name.contains("serialization") ||
                name.contains("json") ||
                name.contains("moshi") ||
                name.contains("gson") ||
                msg.contains("json") ||
                msg.contains("unexpected token") ||
                msg.contains("malformed")
    }
}

data class UiErrorMessage(
    val title: String,
    val message: String,
    val action: String? = null
)

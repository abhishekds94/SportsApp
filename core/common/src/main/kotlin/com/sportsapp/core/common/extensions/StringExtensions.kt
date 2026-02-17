package com.sportsapp.core.common.extensions

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}

fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (length <= maxLength) this
    else "${take(maxLength - ellipsis.length)}$ellipsis"
}

fun String?.orDefault(default: String = "-"): String {
    return this?.takeIf { it.isNotBlank() } ?: default
}

fun String.isValidSearchQuery(): Boolean {
    return this.trim().length >= 3
}
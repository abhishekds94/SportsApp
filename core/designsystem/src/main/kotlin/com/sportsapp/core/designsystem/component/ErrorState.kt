package com.sportsapp.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sportsapp.core.designsystem.R
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorState(
    title: String,
    message: String,
    actionText: String = "Retry",
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_fail,
        title = title,
        subtitle = message,
        actionText = if (onRetry != null) actionText else null,
        onAction = onRetry,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    SportsAppTheme {
        ErrorState(
            title = "Failed to load data",
            message = "Something went wrong on our end.\nPlease try again.",
            actionText = "Retry",
            onRetry = {}
        )
    }
}

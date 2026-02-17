package com.sportsapp.core.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sportsapp.core.designsystem.R
import com.sportsapp.core.designsystem.theme.SportsAppTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoResultsState(
    modifier: Modifier = Modifier
) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_no_results,
        title = "No results found",
        subtitle = "Try searching for another team or\nleague to find what you're looking\nfor.",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun NoResultsStatePreview() {
    SportsAppTheme {
        NoResultsState()
    }
}

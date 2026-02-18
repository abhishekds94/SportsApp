package com.sportsapp.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.R

@Composable
fun IllustratedZeroState(
    @DrawableRes illustrationRes: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = illustrationRes),
            contentDescription = null,
            modifier = Modifier
                .sizeIn(maxWidth = 320.dp, maxHeight = 260.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (actionText != null && onAction != null) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onAction) {
                Text(text = actionText)
            }
        }
    }
}

/**
 * Convenience wrappers (so feature modules don't need to reference R.drawable directly).
 */
@Composable
fun SelectSportZeroState(modifier: Modifier = Modifier) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_sports,
        title = "Select a Sport",
        subtitle = "Choose a sport from the chips above to view leagues and teams",
        modifier = modifier
    )
}

@Composable
fun SelectLeagueZeroState(modifier: Modifier = Modifier) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_league,
        title = "Select a League",
        subtitle = "Choose a league from the dropdown above to view teams and league standings",
        modifier = modifier
    )
}

@Composable
fun OfflineZeroState(
    modifier: Modifier = Modifier,
    onTryAgain: (() -> Unit)? = null
) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_offline,
        title = "You're Offline",
        subtitle = "Check your connection to get back in the game.",
        actionText = if (onTryAgain != null) "Try Again" else null,
        onAction = onTryAgain,
        modifier = modifier
    )
}

@Composable
fun SearchIntroZeroState(modifier: Modifier = Modifier) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_search,
        title = "Find your team",
        subtitle = "Type a name to search for your favorite teams, upcoming matches, or star players.",
        modifier = modifier
    )
}

@Composable
fun FavoritesEmptyZeroState(modifier: Modifier = Modifier) {
    IllustratedZeroState(
        illustrationRes = R.drawable.zs_favs,
        title = "Your Favorites are Empty",
        subtitle = "Follow teams to access their details even when you’re offline. Your favorite teams will be cached here for quick access.",
        modifier = modifier
    )
}

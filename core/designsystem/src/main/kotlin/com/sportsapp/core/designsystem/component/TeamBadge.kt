package com.sportsapp.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.sportsapp.core.designsystem.theme.SportsAppTheme

@Composable
fun TeamBadge(
    badgeUrl: String?,
    teamName: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    if (badgeUrl.isNullOrBlank()) {
        // Show default icon when no URL
        Box(
            modifier = modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "$teamName badge",
                modifier = Modifier.size(size * 0.6f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    } else {
        // Load image from URL with fallback
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(badgeUrl)
                .crossfade(true)
                .build(),
            contentDescription = "$teamName badge",
            contentScale = ContentScale.Fit,
            modifier = modifier.size(size),
            loading = {
                Box(
                    modifier = Modifier.size(size),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size / 2),
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "$teamName badge",
                        modifier = Modifier.size(size * 0.6f),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, name = "No Badge URL")
@Composable
private fun TeamBadgeNoUrlPreview() {
    SportsAppTheme {
        TeamBadge(
            badgeUrl = null,
            teamName = "Arsenal",
            size = 48.dp
        )
    }
}

@Preview(showBackground = true, name = "With Badge URL")
@Composable
private fun TeamBadgeWithUrlPreview() {
    SportsAppTheme {
        TeamBadge(
            badgeUrl = "https://www.thesportsdb.com/images/media/team/badge/xxrxrx1448813340.png",
            teamName = "Arsenal",
            size = 48.dp
        )
    }
}
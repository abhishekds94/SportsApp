package com.sportsapp.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Color.Black,
    primaryContainer = Blue40,
    onPrimaryContainer = Blue80,
    secondary = BlueGrey80,
    onSecondary = Color.Black,
    secondaryContainer = BlueGrey40,
    onSecondaryContainer = BlueGrey80,
    tertiary = Info,
    error = Error,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Blue80,
    onPrimaryContainer = Blue40,
    secondary = BlueGrey40,
    onSecondary = Color.White,
    secondaryContainer = BlueGrey80,
    onSecondaryContainer = BlueGrey40,
    tertiary = Info,
    error = Error,
    background = BackgroundLight,
    onBackground = Grey90,
    surface = SurfaceLight,
    onSurface = Grey90
)

@Composable
fun SportsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
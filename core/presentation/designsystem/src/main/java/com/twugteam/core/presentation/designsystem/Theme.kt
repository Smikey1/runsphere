package com.twugteam.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = RunSphereGreen,
    background = RunSphereBlack,

    // background of dialog, text field
    surface = RunSphereDarkGray,
    secondary = RunSphereWhite,
    tertiary = RunSphereWhite,
    primaryContainer = RunSphereGreen30,
    onPrimary = RunSphereBlack,
    onBackground = RunSphereWhite,

    // Title or Heading Text placed on top of Card, Dialog
    onSurface = RunSphereWhite,
    // value or content for placed on top of Card, Dialog
    onSurfaceVariant = RunSphereGray,
    error = RunSphereDarkRed,
    errorContainer = RunSphereDarkRed5
)

@Composable
fun RunSphereTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // battery %, notification, wifi such icons in the status bar are visible Light Status Bar color
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
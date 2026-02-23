package com.example.mymedifetchproject.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- 1. DARK SCHEME ---
private val DarkColorScheme = darkColorScheme(
    primary = MediFetchTealLight,     // Brighter Teal for Dark Mode
    onPrimary = Color.Black,
    secondary = MediFetchTealContainer,
    background = DarkBackground,      // The 0xFF121212 from Color.kt
    surface = DarkSurface,            // The 0xFF1E1E1E from Color.kt
    onBackground = Color.White,
    onSurface = Color.White,
    error = MedicalError
)

// --- 2. LIGHT SCHEME ---
private val LightColorScheme = lightColorScheme(
    primary = MediFetchTeal,          // Your original 0xFF2C7B76
    onPrimary = Color.White,
    secondary = MediFetchTealContainer,
    background = LightBackground,     // The 0xFFF8FBFB from Color.kt
    surface = LightSurface,           // Pure White cards
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = MedicalError
)

@Composable
fun MyMedifetchProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We choose between light and dark based on the 'darkTheme' boolean
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // This part makes the Status Bar (time/battery at top) match your theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
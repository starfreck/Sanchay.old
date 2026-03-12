package io.github.starfreck.sanchay.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Platform-specific theme logic (e.g., Dynamic Color on Android).
 */
@Composable
expect fun platformColorScheme(
    darkTheme: Boolean,
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme,
): ColorScheme

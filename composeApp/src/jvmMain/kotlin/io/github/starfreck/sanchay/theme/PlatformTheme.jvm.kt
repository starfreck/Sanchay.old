package io.github.starfreck.sanchay.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun platformColorScheme(
    darkTheme: Boolean,
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme,
): ColorScheme = if (darkTheme) darkColorScheme else lightColorScheme

@Composable
actual fun SystemAppearance(darkTheme: Boolean) {
    // Desktop system bar logic can be handled in main.kt or via window properties
}

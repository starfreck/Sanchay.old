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
    // iOS system bars follow the system theme by default in most KMP setups
}

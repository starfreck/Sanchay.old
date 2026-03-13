package io.github.starfreck.sanchay

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.starfreck.sanchay.di.initKoin
import java.awt.Dimension

fun main() = application {
    // Initialize Koin
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Sanchay",
        state = rememberWindowState(size = DpSize(1280.dp, 800.dp))
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}

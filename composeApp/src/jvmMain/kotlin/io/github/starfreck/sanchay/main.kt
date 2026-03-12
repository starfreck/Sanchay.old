package io.github.starfreck.sanchay

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.starfreck.sanchay.di.appModule
import io.github.starfreck.sanchay.di.sharedModule
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() = application {
    // Initialize Koin
    startKoin {
        modules(sharedModule, appModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Sanchay",
        state = rememberWindowState(size = DpSize(1280.dp, 800.dp))
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}
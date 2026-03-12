package io.github.starfreck.sanchay

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.starfreck.sanchay.di.appModule
import io.github.starfreck.sanchay.di.sharedModule
import org.koin.core.context.startKoin

fun main() = application {
    // Initialize Koin
    startKoin {
        modules(sharedModule, appModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Sanchay",
    ) {
        App()
    }
}
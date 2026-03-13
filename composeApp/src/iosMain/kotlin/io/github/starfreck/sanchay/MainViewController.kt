package io.github.starfreck.sanchay

import androidx.compose.ui.window.ComposeUIViewController
import io.github.starfreck.sanchay.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

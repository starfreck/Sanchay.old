package io.github.starfreck.sanchay

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
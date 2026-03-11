package io.github.starfreck.sanchay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SanchayApplication

fun main(args: Array<String>) {
    runApplication<SanchayApplication>(*args)
}

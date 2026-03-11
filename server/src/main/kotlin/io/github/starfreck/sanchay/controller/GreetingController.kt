package io.github.starfreck.sanchay.controller

import io.github.starfreck.sanchay.Greeting
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {

    @GetMapping("/")
    fun greet(): String {
        return "Spring Boot: ${Greeting().greet()}"
    }
}

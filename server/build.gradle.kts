plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlin.plugin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.hibernate.orm)
    alias(libs.plugins.graalvm.native)
    alias(libs.plugins.kotlin.plugin.jpa)
}

group = "io.github.starfreck.sanchay"
version = "0.0.1-SNAPSHOT"
description = "Your personal space to capture notes, manage tasks, and stay on top of your day."

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.shared)
    implementation(libs.springboot.starter.data.jpa)
    implementation(libs.springboot.starter.security)
    implementation(libs.springboot.starter.validation)
    implementation(libs.springboot.starter.webmvc)
    implementation(libs.kotlin.reflect)
    implementation(libs.springdoc.openapi)
    implementation(libs.jackson.module.kotlin)
    developmentOnly(libs.springboot.docker.compose)
    runtimeOnly(libs.postgresql)
    
    testImplementation(libs.springboot.starter.test)
    testImplementation(libs.spring.security.test)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

hibernate {
    enhancement {
        enableAssociationManagement = true
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
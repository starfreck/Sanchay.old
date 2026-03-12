buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        // AGP requires JAXB which was removed in Java 11
        classpath("javax.xml.bind:jaxb-api:2.3.1")
        classpath("org.glassfish.jaxb:jaxb-runtime:2.3.1")
    }
}

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.kotlin.plugin.jpa) apply false
    alias(libs.plugins.hibernate.orm) apply false
    alias(libs.plugins.graalvm.native) apply false
}
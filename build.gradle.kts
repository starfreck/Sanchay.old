buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        // AGP and Kotlin IDE import require JAXB which was removed in Java 11.
        // Using 2.3.x versions because they still use the 'javax.xml.bind' package 
        // that older components expect. Updated to newer maintenance releases
        // for better compatibility with recent JDKs.
        classpath("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
        classpath("org.glassfish.jaxb:jaxb-runtime:2.3.9")
        classpath("com.sun.activation:javax.activation:1.2.0")
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
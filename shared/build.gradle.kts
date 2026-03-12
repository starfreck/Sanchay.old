import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    jvmToolchain(25)
    
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    /*
    js {
        browser()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
    */
    
    // Create a non-web intermediate source set for Room (not supported on JS/WasmJS)
    applyDefaultHierarchyTemplate()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Room KMP
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)

                // Kotlinx
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                
                // Koin (for SharedModule)
                implementation(libs.koin.core)
            }
        }
        
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting
        val iosMain by getting
        val jvmMain by getting
    }
}

// Room KSP for each target (only non-web)
dependencies {
    listOf(
        "kspAndroid",
        "kspIosArm64",
        "kspIosSimulatorArm64",
        "kspJvm",
    ).forEach { targetName ->
        add(targetName, libs.room.compiler)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin.androidLibrary {
    namespace = "io.github.starfreck.sanchay.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    minSdk = libs.versions.android.minSdk.get().toInt()
}

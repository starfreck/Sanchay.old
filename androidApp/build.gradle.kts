plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinJvm) // Requires JVM plugin for base execution
}

android {
    namespace = "io.github.starfreck.sanchay"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "io.github.starfreck.sanchay"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.composeApp) // Depend on the shared UI module
    debugImplementation(libs.compose.uiTooling)
}

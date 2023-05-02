plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "me.him188"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.7.1")
}

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "me.him188.ic.grade.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
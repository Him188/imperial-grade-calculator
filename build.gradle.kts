import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.targets

group = "me.him188"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
}

allprojects {
    afterEvaluate {
        runCatching {
            kotlinExtension.sourceSets.forEach {
                it.languageSettings {
                    enableLanguageFeature("ContextReceivers")
                }
            }
            kotlinExtension.targets.forEach {
                it.compilations.all {
                    kotlinOptions {
                        languageVersion = "1.9"
                        apiVersion = "1.9"
                    }
                }
            }
        }
    }
}
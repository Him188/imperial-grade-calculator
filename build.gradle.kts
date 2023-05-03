import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

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

val optInAnnotations = listOf(
    "androidx.compose.foundation.layout.ExperimentalLayoutApi",
    "androidx.compose.material3.ExperimentalMaterial3Api"
)
subprojects {
    afterEvaluate {
        kotlinExtension.sourceSets.all {
            languageSettings {
                languageVersion = "1.9"
                apiVersion = "1.9"
                enableLanguageFeature("ContextReceivers")

                optInAnnotations.forEach {
                    optIn(it)
                }
            }
        }
    }
}
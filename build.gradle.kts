import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.18.5") // 0.19 requires Kotlin 1.8, but Compose 1.2.2 need exactly 1.7.20
    }
}

allprojects {
    group = "me.him188"
    version = "0.1.0"

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
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



extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "yes")
}

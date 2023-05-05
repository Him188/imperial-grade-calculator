import org.jetbrains.kotlin.gradle.dsl.kotlinExtension

//buildscript {
//    dependencies {
//        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.20.1")
//    }
//}

allprojects {
    group = "me.him188"
    version = "0.1.0"

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }

    configurations.all {
        val conf = this
        // Currently it's necessary to make the android build work properly
        conf.resolutionStrategy.eachDependency {
            val isWasm = conf.name.contains("wasm", true)
            val isJs = conf.name.contains("js", true)
            val isComposeGroup = requested.module.group.startsWith("org.jetbrains.compose")
            val isComposeCompiler = requested.module.group.startsWith("org.jetbrains.compose.compiler")
            if (isComposeGroup && !isComposeCompiler && !isWasm && !isJs) {
                useVersion("1.4.0")
            }
        }
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

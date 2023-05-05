plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "me.him188"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        jvmToolchain(11)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material3)
//                api(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                api(project(":calculator"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmBaseMain by creating {
            dependsOn(commonMain)
            dependencies {
            }
        }
        val androidMain by getting {
            dependsOn(jvmBaseMain)
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.0")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependsOn(jvmBaseMain)
            dependencies {
                api(compose.preview)
                api(compose.uiTooling)
                api(compose.runtime)
                api("dev.dirs:directories:26")
            }
        }
        val desktopTest by getting {
            dependencies {

            }
        }
//        val jsWasmMain by creating {
//            dependsOn(commonMain)
//        }
//        val wasmMain by getting {
//            dependencies {
//                implementation(compose.runtime)
//                api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1-wasm0")
//                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1-wasm0")
//            }
//        }
//        val jsMain by getting {
//            dependsOn(jsWasmMain)
//            dependencies {
////                implementation(compose.html.core)
//            }
//        }
//        val jsTest by getting {
//            dependencies {
//                implementation(kotlin("test-js"))
//            }
//        }
    }
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
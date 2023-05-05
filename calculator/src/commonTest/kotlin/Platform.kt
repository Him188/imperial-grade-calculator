package me.him188.ic.grade.common

expect fun currentPlatform(): Platform

enum class Platform {
    JS,
    JVM,
    ANDROID,
}
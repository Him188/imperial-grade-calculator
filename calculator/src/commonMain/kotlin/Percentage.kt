package me.him188.ic.grade.common

import kotlin.jvm.JvmInline

@JvmInline
value class Percentage(
    val value: Int,
)

val Int.percent: Percentage get() = Percentage(this)
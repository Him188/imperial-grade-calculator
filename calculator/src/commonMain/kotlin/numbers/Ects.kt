package me.him188.ic.grade.common.numbers

import kotlin.jvm.JvmInline


@JvmInline
value class Ects(
    val value: Double,
) {
    override fun toString(): String {
        return if (value.toInt().toDouble() == value) {
            "${value.toInt()} ECTS"
        } else {
            "$value ECTS"
        }
    }
}

val Int.ects: Ects get() = Ects(this.toDouble())
val Double.ects: Ects get() = Ects(this)

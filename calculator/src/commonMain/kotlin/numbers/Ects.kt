package me.him188.ic.grade.common.numbers

import androidx.compose.runtime.Immutable
import kotlin.jvm.JvmInline


@JvmInline
@Immutable
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

    companion object {
        val ZERO: Ects = 0.ects
    }
}

val Int.ects: Ects get() = Ects(this.toDouble())
val Double.ects: Ects get() = Ects(this)

operator fun Ects.plus(value: Ects) = this.value.plus(value.value).ects

operator fun Ects.times(percentage: Percentage) = this.value.times(percentage).ects
operator fun Ects.times(value: Double) = this.value.times(value).ects
operator fun Double.times(value: Ects) = this.times(value.value).ects
operator fun Percentage.times(value: Ects): Ects = this.value.times(value.value).ects

operator fun Ects.div(value: Ects): Double = this.value.div(value.value)


fun Array<Ects>.sum(): Ects = this.sumOf { it.value }.ects
fun Iterable<Ects>.sum(): Ects = this.sumOf { it.value }.ects
fun Array<Ects?>.sumNotNull(): Ects = this.sumOf { it?.value ?: 0.0 }.ects
fun Iterable<Ects?>.sumNotNull(): Ects = this.sumOf { it?.value ?: 0.0 }.ects

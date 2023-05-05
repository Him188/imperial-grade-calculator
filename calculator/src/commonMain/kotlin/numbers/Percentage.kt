package me.him188.ic.grade.common.numbers

import androidx.compose.runtime.Immutable
import kotlin.math.pow
import kotlin.math.round

@JvmInline
@Immutable
value class Percentage(
    private val v: Double,
) : Comparable<Percentage> {
    override fun compareTo(other: Percentage): Int {
        val thisV = this.v
        val otherV = other.v
        return thisV.compareTo(otherV)
    }

    override fun toString(): String {
        return value.toPercentageString()
    }

    fun toString(digits: Int): String {
        return value.toPercentageString(digits)
    }

    val percents: Int
        get() {
            val v = this.v
            return (v * 100.0).toInt()
        }

    val value: Double
        get() {
            return this.v
        }

    companion object {
        val ZERO = 0.0.toPercentage()
    }
}

val Int.percent: Percentage
    get() {
        if (this == 0) return Percentage.ZERO
        return Percentage(this.toDouble() / 100)
    }

fun Double.toPercentage(): Percentage = Percentage(this)


fun Double.toPercentageString(digits: Int = 2): String {
    return "${times(100).roundToString(digits)}%"
}

fun Double.roundToString(digits: Int = 2): String {
    if (digits == 0) {
        return round(this).toLong().toString()
    }
    val pow = 10.0.pow(digits).toInt()
    val res = round(this.times(pow)).div(pow).toString()
    val dot = res.indexOfLast { it == '.' }
    if (dot == -1) {
        return "$res." + "0".repeat(digits)
    }
    // 1.0
    val numberOfDigits = res.length - dot - 1
    if (numberOfDigits < digits) {
        return res + "0".repeat(digits - numberOfDigits)
    }

    return res
//    val x = round(this * pow)
//    val integer = (x / pow).toInt()
//    val fractional = (x % pow).toInt().toString()
//    return "$integer.$fractional" + "0".repeat((digits - fractional.length).coerceAtLeast(0))
}

operator fun Int.times(percentage: Percentage): Double = this.times(percentage.value)
operator fun Int.div(percentage: Percentage): Double = this.div(percentage.value)
operator fun Double.times(percentage: Percentage): Double = this.times(percentage.value)
operator fun Double.div(percentage: Percentage): Double = this.div(percentage.value)
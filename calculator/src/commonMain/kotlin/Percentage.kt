package me.him188.ic.grade.common

import kotlin.jvm.JvmInline
import kotlin.math.round

@JvmInline
value class Percentage(
    private val v: Any,
) {
    init {
        if (v is Int) {
            require(v in 0..100) { "Invalid percents: $v" }
        } else {
            v as Double
            require(v in 0.0..1.0) { "Invalid percentage: $v" }
        }
    }

    override fun toString(): String {
        val x = round(value * 10000)
        val integer = (x / 100).toInt()
        val fractional = (x % 100).toInt()
        return "$integer.$fractional%"
    }

    val percents: Int
        get() {
            val v = this.v
            return if (v is Int) v else ((v as Double) * 100.0).toInt()
        }

    val value: Double
        get() {
            val v = this.v
            return if (v is Int) (v.toDouble() / 100) else v as Double
        }
}

val Int.percent: Percentage get() = Percentage(this)
fun Double.toPercentage(): Percentage = Percentage(this)

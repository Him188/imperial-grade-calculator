package me.him188.ic.grade.common

import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.percent

enum class GradeLetter(val threshold: Percentage) {
    A(70.percent),
    B(60.percent),
    C(50.percent),
    D(40.percent),
    F(0.percent);

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun fromMarks(percentage: Percentage): GradeLetter {
            for (entry in entries) {
                if (percentage >= entry.threshold) return entry
            }
            return F
        }
    }
}

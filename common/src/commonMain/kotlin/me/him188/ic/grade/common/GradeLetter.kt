package me.him188.ic.grade.common

enum class GradeLetter(val threshold: Double) {
    A(0.7),
    B(0.6),
    C(0.5),
    D(0.4),
    F(0.0);

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun fromMarks(value: Double): GradeLetter {
            for (entry in entries) {
                if (value >= entry.threshold) return entry
            }
            return F
        }
    }
}

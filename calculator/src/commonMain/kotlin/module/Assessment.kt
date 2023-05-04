package me.him188.ic.grade.common.module

import me.him188.ic.grade.common.numbers.Percentage

data class Assessment(
    val name: String,
    val category: Category,
    val maxGrade: Int,
    val creditShare: Percentage?,
) {
    init {
        require(maxGrade >= 0) { "Invalid maxGrade: $maxGrade" }
    }
}
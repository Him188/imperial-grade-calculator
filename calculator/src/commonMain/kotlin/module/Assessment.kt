package me.him188.ic.grade.common.module

import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage

class Assessment(
    val name: String,
    val category: Category,
    val availableMarks: Int,
    val creditShare: Percentage,
    val availableEcts: Ects,
) {
    init {
        require(availableMarks >= 0) { "Invalid availableMarks: $availableMarks" }
    }

    companion object {
        const val NAME_EXAMINATION = "Examination"
    }
}
package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.times

class AssessmentResult(
    val assessment: Assessment,
) {
    val resultGrade: MutableStateFlow<Int?> = MutableStateFlow(null)
    val creditShare get() = assessment.creditShare ?: Percentage.ZERO

    private val percentage: Flow<Double> = resultGrade.map { (it?.toDouble() ?: 0.0) / assessment.maxGrade }
    val percentageContributionInModule: Flow<Double> = percentage.map { it * creditShare }

    fun setResult(grades: Int?) {
        this.resultGrade.value = grades
    }
}
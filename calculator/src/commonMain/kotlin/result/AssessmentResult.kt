package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.*
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.times
import me.him188.ic.grade.common.numbers.toPercentage

class AssessmentResult(
    val assessment: Assessment,
) {
    private val _awardedMarks: MutableStateFlow<Int?> = MutableStateFlow(null)
    val awardedMarks: StateFlow<Int?> = _awardedMarks.asStateFlow()

    val awardedPercentage: Flow<Percentage> =
        awardedMarks.map { (it.toDoubleOrZero() / assessment.availableMarks).toPercentage() }

    val awardedCredits: Flow<Ects> = awardedPercentage.map { it * availableEcts }

    fun setAwardedMarks(grades: Int?) {
        _awardedMarks.value = grades
    }
}

fun AssessmentResult.availablePercentageInStandaloneModule(module: SubModule): Percentage {
    return (this.assessment.creditShare.value * module.creditShare).toPercentage()
}

val AssessmentResult.availableEcts get() = assessment.availableEcts

internal fun Int?.toDoubleOrZero(): Double {
    return this?.toDouble() ?: 0.0
}
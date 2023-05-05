package me.him188.ic.grade.common.result

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.*
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.times
import me.him188.ic.grade.common.numbers.toPercentage

@Stable
class AssessmentResult(
    val assessment: Assessment,
) : ChangeObservable {
    private val _awardedMarks: MutableStateFlow<Int?> = MutableStateFlow(null)
    val awardedMarks: StateFlow<Int?> = _awardedMarks.asStateFlow()

    val awardedPercentage: Flow<Percentage?> =
        awardedMarks.map {
            if (it == null) return@map null

            val availableMarks = assessment.availableMarks
            if (availableMarks == 0) {
                null
            } else {
                (it.toDouble() / availableMarks).toPercentage()
            }
        }

    val awardedCredits: Flow<Ects?> = awardedPercentage.map { it?.times(availableEcts) }

    override val changed: Flow<Any?> = merge(awardedMarks)

    val inputted: Flow<Assessment?> = _awardedMarks.map { if (it == null) null else assessment }

    fun setAwardedMarks(grades: Int?) {
        _awardedMarks.value = grades
    }
}

val AssessmentResult.name get() = this.assessment.name

@Stable
fun AssessmentResult.availablePercentageInStandaloneModule(module: SubModule): Percentage {
    return (this.assessment.creditShare.value * module.creditShare).toPercentage()
}

val AssessmentResult.availableEcts get() = assessment.availableEcts

internal fun Int?.toDoubleOrZero(): Double {
    return this?.toDouble() ?: 0.0
}
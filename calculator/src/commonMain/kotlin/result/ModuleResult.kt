package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import me.him188.ic.grade.common.module.Module
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.times


sealed class ModuleResult<M : Module>(
    val module: M,
) {
    val assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) }

    abstract val totalPercentage: Flow<Double>
    val totalPercentageInAssessments: Flow<Double> =
        combine(assessmentResults.map { it.percentageContributionInModule }) {
            it.sum()
        }
}


class StandaloneModuleResult(
    module: StandaloneModule,
//    assessmentResults: List<AssessmentResult> = module.assessments
) : ModuleResult<StandaloneModule>(module) {
    val submoduleResults: List<SubmoduleResult> = module.submodules.map { SubmoduleResult(it) }
    private val totalPercentageInSubmodules: Flow<Double> =
        combine(submoduleResults.map { it.percentageContribution }) { it.sum() }

    override val totalPercentage: Flow<Double> =
        combine(totalPercentageInSubmodules, totalPercentageInAssessments) { a, b -> a + b }
}

class SubmoduleResult(module: SubModule) : ModuleResult<SubModule>(module) {
    val percentageContribution: Flow<Double> = totalPercentageInAssessments.map { it * module.creditShare }
    override val totalPercentage: Flow<Double>
        get() = totalPercentageInAssessments
}
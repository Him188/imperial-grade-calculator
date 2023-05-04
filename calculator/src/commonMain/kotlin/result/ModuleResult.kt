package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import me.him188.ic.grade.common.module.Module
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.*


sealed class ModuleResult<M : Module>(
    val module: M,
) {
    val assessmentResults: List<AssessmentResult> by lazy {
        module.assessments.map {
            AssessmentResult(it)
        }
    }

    abstract val awardedCredits: Flow<Ects>
    val awardedPercentageInThisModule: Flow<Percentage> by lazy { awardedCredits.map { (it / module.availableCredits).toPercentage() } }

    internal val awardedCreditsFromAssessments: Flow<Ects> =
        combine(assessmentResults.map { it.awardedCredits }) { it.sum() }
}


class StandaloneModuleResult(
    module: StandaloneModule,
//    assessmentResults: List<AssessmentResult> = module.assessments
) : ModuleResult<StandaloneModule>(module) {
    val submoduleResults: List<SubmoduleResult> by lazy {
        module.submodules.map {
            SubmoduleResult(it)
        }
    }
    private val awardedCreditsFromSubmodules: Flow<Ects> by lazy { combine(submoduleResults.map { it.awardedCredits }) { it.sum() } }

    override val awardedCredits: Flow<Ects> by lazy {
        if (submoduleResults.isEmpty() && assessmentResults.isEmpty()) return@lazy emptyFlow()

        when {
            submoduleResults.isEmpty() -> {
                awardedCreditsFromAssessments
            }

            assessmentResults.isEmpty() -> {
                awardedCreditsFromSubmodules
            }

            else -> {
                combine(
                    awardedCreditsFromSubmodules,
                    awardedCreditsFromAssessments
                ) { a, b -> a + b }
            }
        }
    }
}

class SubmoduleResult(module: SubModule) : ModuleResult<SubModule>(
    module
) {
    val awardedPercentageInParentModule: Flow<Percentage> by lazy { awardedPercentageInThisModule.map { (it.value * module.creditShare).toPercentage() } }

    override val awardedCredits: Flow<Ects>
        get() = awardedCreditsFromAssessments
}
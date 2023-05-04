package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.*
import me.him188.ic.grade.common.module.Module
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.*


sealed class ModuleResult<M : Module>(
    val module: M,
    val assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) }
) : ChangeObservable {
    abstract val awardedCredits: Flow<Ects>
    val awardedPercentageInThisModule: Flow<Percentage> by lazy { awardedCredits.map { (it / availableCredits).toPercentage() } }

    internal val awardedCreditsFromAssessments: Flow<Ects> =
        combine(assessmentResults.map { it.awardedCredits }) { it.sum() }

    override val changed: Flow<Any?> = assessmentResults.map { it.awardedMarks }.merge()
}

val ModuleResult<*>.name get() = this.module.name
val ModuleResult<*>.availableCredits get() = this.module.availableCredits


class StandaloneModuleResult(
    module: StandaloneModule,
    assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) },
    val submoduleResults: List<SubmoduleResult> = module.submodules.map { SubmoduleResult(it) }
) : ModuleResult<StandaloneModule>(module, assessmentResults) {
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

class SubmoduleResult : ModuleResult<SubModule> {


    constructor(module: SubModule) : super(module)
    constructor(module: SubModule, assessmentResults: List<AssessmentResult>) : super(module, assessmentResults)

    val awardedPercentageInParentModule: Flow<Percentage> by lazy { awardedPercentageInThisModule.map { (it.value * module.creditShare).toPercentage() } }

    override val awardedCredits: Flow<Ects>
        get() = awardedCreditsFromAssessments
}
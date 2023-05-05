package me.him188.ic.grade.common.result

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.*
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.module.Module
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.module.SubModule
import me.him188.ic.grade.common.numbers.*


@Stable
sealed class ModuleResult<M : Module>(
    val module: M,
    val assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) }
) : ChangeObservable {
    abstract val awardedCredits: Flow<Ects>

    protected abstract val allModules: List<ModuleResult<*>>

    val awardedPercentageInThisModule: Flow<Percentage?> by lazy {
        combine(awardedCredits, inputtedTotalCredits) { awardedCredits, availableCredits ->
            if (availableCredits == 0.ects) {
                null
            } else {
                (awardedCredits / availableCredits).toPercentage()
            }
        }
    }

    val isPercentageProjected: Flow<Boolean> by lazy {
        inputtedTotalCredits.map { it != module.availableCredits }
    }

    internal val awardedCreditsFromAssessments: Flow<Ects> =
        combine(assessmentResults.map { it.awardedCredits }) { it.sumNotNull() }


    private val inputtedAssessments: Flow<List<Assessment>> =
        combine(assessmentResults.map { it.inputted }) { it.filterNotNull() }

    private val inputtedTotalCredits: Flow<Ects> by lazy {
        combine(allModules.map { it.inputtedAssessments }) { array ->
            array.sumOf { assessments -> assessments.sumOf { it.availableEcts.value } }.ects
        }
    }

    override val changed: Flow<Any?> = assessmentResults.map { it.awardedMarks }.merge()
}

@Stable
val ModuleResult<*>.name get() = this.module.name

@Stable
val ModuleResult<*>.availableCredits get() = this.module.availableCredits


class StandaloneModuleResult(
    module: StandaloneModule,
    assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) },
    @Stable
    val submoduleResults: List<SubmoduleResult> = module.submodules.map { SubmoduleResult(it) }
) : ModuleResult<StandaloneModule>(module, assessmentResults) {
    private val awardedCreditsFromSubmodules: Flow<Ects> by lazy { combine(submoduleResults.map { it.awardedCredits }) { it.sum() } }

    @Stable
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
    override val allModules: List<ModuleResult<*>> = listOf(this) + submoduleResults
}

@Stable
class SubmoduleResult : ModuleResult<SubModule> {


    constructor(module: SubModule) : super(module)
    constructor(module: SubModule, assessmentResults: List<AssessmentResult>) : super(module, assessmentResults)

    val awardedPercentageInParentModule: Flow<Percentage?> by lazy {
        awardedPercentageInThisModule.map {
            it?.value?.times(module.creditShare)?.toPercentage()
        }
    }

    override val awardedCredits: Flow<Ects>
        get() = awardedCreditsFromAssessments
    override val allModules: List<ModuleResult<*>> = listOf(this)
}
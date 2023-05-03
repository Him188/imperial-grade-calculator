package me.him188.ic.grade.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


class StandaloneModuleResult(
    module: StandaloneModule,
//    assessmentResults: List<AssessmentResult> = module.assessments
) : ModuleResult<StandaloneModule>(module) {
    val submoduleResults: List<SubmoduleResult> = module.submodules.map { SubmoduleResult(it) }
}

class SubmoduleResult(module: SubModule) : ModuleResult<SubModule>(module) {
//    val percentage: Flow<Double> = credits.map { module.creditShare }
}

sealed class ModuleResult<M : Module>(
    val module: M,
) {
    val assessmentResults: List<AssessmentResult> = module.assessments.map { AssessmentResult(it) }

    val totalPercentage: Flow<Double> =
//        val scope = CoroutineScope(parentCoroutineContext + Job(parentCoroutineContext[Job]))
        combine(assessmentResults.map { it.percentageContributionInModule }) {
            it.sum()
        }
//        flow<Double> {
//            val currentResults = arrayListOf<Double>()
//            suspend fun calculateResult() {
//                emit(currentResults.sum())
//            }
//
//            val dispatcher = (scope.coroutineContext[ContinuationInterceptor] as? CoroutineDispatcher
//                ?: Dispatchers.Default).limitedParallelism(1)
//
//            for ((index, assessmentResult) in assessmentResults.withIndex()) {
//                scope.launch(dispatcher) {
//                    assessmentResult.resultCredits.collect { newResult ->
//                        currentResults[index] = newResult
//                        calculateResult()
//                    }
//                }
//            }
//        }
}

class AssessmentResult(
    val assessment: Assessment,
) {
    val resultGrade: MutableStateFlow<Int?> = MutableStateFlow(null)
    val creditShare get() = assessment.creditShare ?: Percentage.ZERO

    private val assessmentPercentage: Flow<Double> = resultGrade.map { (it?.toDouble() ?: 0.0) / assessment.maxGrade }
    val percentageContributionInModule: Flow<Double> = assessmentPercentage.map { it * creditShare }

    fun setResult(grades: Int?) {
        this.resultGrade.value = grades
    }
}


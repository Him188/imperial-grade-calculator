package me.him188.ic.grade.common

import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.result.*

internal actual fun deserializeYearResultData(data: String, applyTo: AcademicYearResult) {
    val json = parse(data)
    applyTo.applyData(json)
}

@JsFun("(data) => JSON.parse(data)")
internal external fun parse(data: String): AcademicYearResultData
internal actual fun serializeYearResultData(data: AcademicYearResult): String {
    return stringify(data.toData())
}


@JsFun("(data) => JSON.stringify(data)")
internal external fun stringify(data: AcademicYearResultData): String

class AssessmentResultData(
    val name: String,
    val awardedMarks: Int?,
)

fun AssessmentResult.toData(): AssessmentResultData {
    return AssessmentResultData(assessment.name, awardedMarks.value)
}

fun AssessmentResultData.toModel(assessment: Assessment): AssessmentResult {
    return AssessmentResult(assessment).also { it.setAwardedMarks(awardedMarks) }
}

sealed class ModuleResultData {
    abstract val name: String
    abstract val assessmentResults: List<AssessmentResultData>
}

class StandaloneModuleResultData(
    override val name: String,
    override val assessmentResults: List<AssessmentResultData>,
    val submoduleResults: List<SubmoduleResultData>,
) : ModuleResultData()

class SubmoduleResultData(
    override val name: String,
    override val assessmentResults: List<AssessmentResultData>
) : ModuleResultData()

fun StandaloneModuleResult.toData(): ModuleResultData = StandaloneModuleResultData(
    this.module.name,
    this.assessmentResults.map { it.toData() },
    this.submoduleResults.map { it.toData() }
)

fun SubmoduleResult.toData(): SubmoduleResultData =
    SubmoduleResultData(
        this.module.name,
        this.assessmentResults.map { it.toData() }
    )

fun AcademicYearResult.toData(): AcademicYearResultData =
    AcademicYearResultData(this.moduleResults.map { it.toData() })

fun AcademicYearResult.applyData(data: AcademicYearResultData) {
    for (moduleResult in this.moduleResults) {
        val moduleResultData = data.moduleResults.find { it.name == moduleResult.name } ?: continue
        moduleResult.applyData(moduleResultData)

    }
}

private fun ModuleResult<*>.applyData(
    moduleResultData: ModuleResultData
) {
    for (assessmentResult in assessmentResults) {
        val assessmentResultData =
            moduleResultData.assessmentResults.find { it.name == assessmentResult.name } ?: continue
        assessmentResult.setAwardedMarks(assessmentResultData.awardedMarks)
    }
    if (this is StandaloneModuleResult && moduleResultData is StandaloneModuleResultData) {
        for (submoduleResult in submoduleResults) {
            val submoduleResultData =
                moduleResultData.submoduleResults.find { it.name == submoduleResult.name } ?: continue

            submoduleResult.applyData(submoduleResultData)
        }
    }
}

class AcademicYearResultData(
    val moduleResults: List<ModuleResultData>
)

package me.him188.ic.grade.common.module

import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.percent
import me.him188.ic.grade.common.numbers.times
import me.him188.ic.grade.common.year.AcademicYearBuilderDsl
import me.him188.ic.grade.common.year.ModuleCreditShareRefiner

@AcademicYearBuilderDsl
sealed class ModuleBuilder(
    val name: String,
) {
    internal val assessments = mutableListOf<RawAssessment>()

    internal fun assessment(assessment: RawAssessment) {
        this.assessments.add(assessment)
    }

    abstract fun build(): Module
}

class StandaloneModuleBuilder(name: String, private val availableEcts: Ects) : ModuleBuilder(name) {
    private val submodules = mutableListOf<SubModule>()

    fun coursework(name: String, maxGrade: Int, share: Percentage? = null) {
        assessment(RawAssessment(name, Category.COURSEWORK, maxGrade, share))
    }

    fun exam(name: String? = null, share: Percentage = 85.percent) {
        assessment(RawAssessment(name ?: Assessment.NAME_EXAMINATION, Category.EXAMS, 100, share))
    }

    fun submodule(name: String, creditShare: Percentage, action: context(SubModuleBuilder) () -> Unit = {}): SubModule {
        val sub = SubModuleBuilder(name, creditShare, availableEcts * creditShare).apply(action).build()
        submodules.add(sub)
        return sub
    }

    override fun build(): StandaloneModule {
        return StandaloneModule(
            name, availableEcts, submodules,
            ModuleCreditShareRefiner.refineAssessmentCredits(assessments, availableEcts)
        )
    }
}

class SubModuleBuilder(
    name: String,
    private val creditShare: Percentage,
    private val availableEcts: Ects,
) : ModuleBuilder(name) {
    override fun build(): SubModule = SubModule(
        name,
        availableEcts,
        creditShare,
        ModuleCreditShareRefiner.refineAssessmentCredits(assessments, availableEcts)
    )

    fun coursework(name: String, maxGrade: Int, share: Percentage) {
        assessment(RawAssessment(name, Category.COURSEWORK, maxGrade, share))
    }
}


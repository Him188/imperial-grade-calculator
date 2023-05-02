package me.him188.ic.grade.common


@AcademicYearBuilderDsl
class AcademicYearBuilder {
    private val modules = mutableListOf<Module>()

    fun module(name: String, ects: Double, action: context(StandaloneModuleBuilder) () -> Unit): Module {
        val module = StandaloneModuleBuilder(name, ects).apply(action).build()
        modules.add(module)
        return module
    }

    fun build(): AcademicYear = AcademicYear(modules)
}

@DslMarker
annotation class AcademicYearBuilderDsl

@AcademicYearBuilderDsl
fun AcademicYearBuilder.module(name: String, ects: Int, action: context(StandaloneModuleBuilder) () -> Unit): Module {
    return module(name, ects.toDouble(), action)
}

@AcademicYearBuilderDsl
sealed class ModuleBuilder(
    val name: String,
) {
    protected val assessments = mutableListOf<Assessment>()

    fun assessment(assessment: Assessment) {
        this.assessments.add(assessment)
    }

    abstract fun build(): Module
}

class StandaloneModuleBuilder(name: String, private val credits: Double) : ModuleBuilder(name) {
    private val submodules = mutableListOf<SubModule>()

    fun coursework(name: String, maxGrade: Int, share: Percentage? = null) {
        assessment(Assessment(name, Category.COURSEWORK, maxGrade, share))
    }

    fun exam(name: String? = null, share: Percentage = 85.percent) {
        assessment(Assessment(name ?: "Examination", Category.EXAMS, 100, share))
    }

    fun submodule(name: String, creditShare: Percentage, action: context(SubModuleBuilder) () -> Unit = {}): SubModule {
        val sub = SubModuleBuilder(name, creditShare).apply(action).build()
        submodules.add(sub)
        return sub
    }

    override fun build(): StandaloneModule = StandaloneModule(name, credits, submodules, assessments)
}


class SubModuleBuilder(
    name: String,
    private val creditShare: Percentage
) : ModuleBuilder(name) {
    override fun build(): SubModule = SubModule(name, creditShare, assessments)

    fun coursework(name: String, maxGrade: Int, share: Percentage) {
        assessment(Assessment(name, Category.COURSEWORK, maxGrade, share))
    }
}

class AcademicYear(
    val modules: List<Module>,
)

fun buildAcademicYear(builderAction: context(AcademicYearBuilder) () -> Unit): AcademicYear {
    return AcademicYearBuilder().apply(builderAction).build()
}

package me.him188.ic.grade.common

data class ModuleResult(
    val module: Module,
    val coursework: Double,
    val exams: Double
)

data class Assessment(
    val name: String,
    val category: Category,
    val maxGrade: Int,
    val creditShare: Percentage?,
) {
    init {
        require(maxGrade >= 0) { "Invalid maxGrade: $maxGrade" }
    }
}

data class Category(
    val name: String,
) {
    companion object {
        val COURSEWORK = Category("Coursework")
        val EXAMS = Category("Exams")
    }
}

sealed interface Module {
    val name: String
    val assessments: List<Assessment>
}


data class StandaloneModule(
    override val name: String,
    val credits: Ects,
    val submodules: List<SubModule>,
    override val assessments: List<Assessment>
) : Module

data class SubModule(
    override val name: String,
    val creditShare: Percentage,
    override val assessments: List<Assessment>
) : Module
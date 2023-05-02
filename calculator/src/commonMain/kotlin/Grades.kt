package me.him188.ic.grade.common

class CourseResult(
    val course: Course,
    val coursework: Double,
    val exams: Double
)

class Assessment(
    val name: String,
    val category: Category,
    val creditContribution: Double,
)

class Category(
    val name: String,
) {
    companion object {
        val COURSEWORK = Category("Coursework")
        val EXAMS = Category("Exams")
    }
}

data class Course(
    val name: String,
    val credits: String,
    val creditDistribution: Map<Category, Category>,
    val assessments: List<Assessment>,
)


class CreditDistribution(
    val coursework: Double,
    val exams: Double,
) {
    companion object {
        val DEFAULT = CreditDistribution(0.15, 0.85)
    }
}
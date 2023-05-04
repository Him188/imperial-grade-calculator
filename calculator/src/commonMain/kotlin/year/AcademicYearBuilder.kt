package me.him188.ic.grade.common.year

import me.him188.ic.grade.common.module.Module
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.module.StandaloneModuleBuilder
import me.him188.ic.grade.common.numbers.Ects


@AcademicYearBuilderDsl
class AcademicYearBuilder {
    private val modules = mutableListOf<StandaloneModule>()

    fun module(name: String, ects: Ects, action: context(StandaloneModuleBuilder) () -> Unit): Module {
        val module = StandaloneModuleBuilder(name, ects).apply(action).build()
        modules.add(module)
        return module
    }

    fun build(): AcademicYear = AcademicYear(modules)
}

class AcademicYear(
    val modules: List<StandaloneModule>,
)

fun buildAcademicYear(builderAction: context(AcademicYearBuilder) () -> Unit): AcademicYear {
    return AcademicYearBuilder().apply(builderAction).build()
}

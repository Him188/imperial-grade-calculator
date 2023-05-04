package me.him188.ic.grade.common.module

import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage

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
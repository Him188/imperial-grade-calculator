package me.him188.ic.grade.common.module

import androidx.compose.runtime.Immutable
import me.him188.ic.grade.common.numbers.Ects
import me.him188.ic.grade.common.numbers.Percentage

@Immutable
sealed interface Module {
    val name: String
    val availableCredits: Ects

    val assessments: List<Assessment>
}

@Immutable
data class StandaloneModule(
    override val name: String,
    override val availableCredits: Ects,
    val submodules: List<SubModule>,
    override val assessments: List<Assessment>
) : Module

@Immutable
data class SubModule(
    override val name: String,
    override val availableCredits: Ects,
    /**
     * How much this submodule contributes to its parent
     */
    val creditShare: Percentage,
    override val assessments: List<Assessment>
) : Module
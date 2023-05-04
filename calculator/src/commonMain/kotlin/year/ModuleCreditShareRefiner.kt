package me.him188.ic.grade.common.year

import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.numbers.toPercentage

object ModuleCreditShareRefiner {
//    fun refineCredits(submodules: List<SubModule>, assessments: List<Assessment>) {
//        if (assessments.isEmpty() && submodules.isEmpty()) {
//            return
//        }
//        if (assessments.isNotEmpty() && submodules.isNotEmpty()) {
//            throw IllegalStateException("Cannot automatically distribute credits: both assessments and submodules are not empty")
//        }
//
//        if (assessments.isNotEmpty()) {
//        }
//    }

    fun refineAssessmentCredits(assessments: List<Assessment>): List<Assessment> {
        val (specified, unspecified) = assessments.partition { it.creditShare != null }
        val availablePercents = (1 - specified.sumOf { it.creditShare!!.value })
        if (availablePercents !in 0.0..1.0) {
            throw IllegalArgumentException("Illegal creditShare values: $assessments")
        }

        val average = availablePercents / unspecified.size
        return unspecified.map { it.copy(creditShare = average.toPercentage()) } + specified
    }
}
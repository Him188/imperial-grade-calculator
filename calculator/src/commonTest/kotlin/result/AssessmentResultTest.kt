package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.module.Category
import me.him188.ic.grade.common.numbers.ects
import me.him188.ic.grade.common.numbers.percent
import me.him188.ic.grade.common.numbers.toPercentage
import kotlin.test.Test
import kotlin.test.assertEquals

class AssessmentResultTest {

    @Test
    fun canCalculate() = runTest {
        val assessment = AssessmentResult(
            Assessment(
                "CW",
                Category.COURSEWORK,
                availableMarks = 100,
                creditShare = 15.percent,
                availableEcts = 5.ects,
            ),
        )
        val awarded = assessment.awardedPercentage
        val marks = assessment.awardedMarks
        val credits = assessment.awardedCredits
        assessment.setAwardedMarks(80)
        assertEquals(80, marks.first())
        assertEquals(0.8.toPercentage(), awarded.first())
        assertEquals((80.0 / 100 * 5).ects, credits.first())
    }

    @Test
    fun canUpdate() = runTest {
        val assessment = AssessmentResult(
            Assessment(
                "CW",
                Category.COURSEWORK,
                availableMarks = 100,
                creditShare = 15.percent,
                availableEcts = 5.ects,
            ),
        )
        val awarded = assessment.awardedPercentage
        val marks = assessment.awardedMarks
        val credits = assessment.awardedCredits
        assessment.setAwardedMarks(60)
        assessment.setAwardedMarks(80)
        assertEquals(80, marks.first())
        assertEquals(0.8.toPercentage(), awarded.first())
        assertEquals((80.0 / 100 * 5).ects, credits.first())
    }
}
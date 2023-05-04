package me.him188.ic.grade.common.result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.module.Category
import me.him188.ic.grade.common.module.StandaloneModule
import me.him188.ic.grade.common.numbers.ects
import me.him188.ic.grade.common.numbers.percent
import me.him188.ic.grade.common.numbers.toPercentage
import kotlin.test.Test
import kotlin.test.assertEquals

class StandaloneModuleResultTest {
    private val module = StandaloneModuleResult(
        StandaloneModule(
            "Test",
            5.ects,
            listOf(),
            listOf(
                Assessment(
                    "CW1",
                    Category.COURSEWORK,
                    availableMarks = 100,
                    creditShare = 15.percent,
                    availableEcts = (5 * 0.15).ects,
                ),
                Assessment(
                    "CW2",
                    Category.COURSEWORK,
                    availableMarks = 100,
                    creditShare = 85.percent,
                    availableEcts = (5 * 0.85).ects,
                )

            )
        ),
    )
    private val cw1 = module.assessmentResults.single { it.assessment.name == "CW1" }
    private val cw2 = module.assessmentResults.single { it.assessment.name == "CW2" }

    @Test
    fun assessmentChangesTriggerUpdate() = runTest {
        val awardedCredits = module.awardedCredits.shareLazilyIn(backgroundScope)
        val awardedPercentage = module.awardedPercentageInThisModule.shareLazilyIn(backgroundScope)

        cw1.setAwardedMarks(80)
        cw2.setAwardedMarks(100)
        assertEquals((80.0 / 100 * (5 * 0.15) + 100 / 100 * (5 * 0.85)).ects, awardedCredits.first())
        assertEquals(0.97.toPercentage(), awardedPercentage.first())
    }
}

private fun <T> Flow<T>.shareLazilyIn(scope: CoroutineScope, reply: Int = 0): SharedFlow<T> {
    return this.shareIn(scope, SharingStarted.Lazily, reply)
}

package me.him188.ic.grade.common.numbers

import kotlin.test.Test
import kotlin.test.assertEquals

class PercentageTest {
    @Test
    fun value() {
        assertEquals(0.01, 1.percent.value)
        assertEquals(0.85, 85.percent.value)
        assertEquals(0.11, 0.11.toPercentage().value)
    }

    @Test
    fun percents() {
        assertEquals(1, 1.percent.percents)
        assertEquals(85, 85.percent.percents)
        assertEquals(11, 0.11.toPercentage().percents)
    }

    @Test
    fun toStringTest() {
        assertEquals("1.00%", 1.percent.toString())
        assertEquals("85.00%", 85.percent.toString())
        assertEquals("11.00%", 0.11.toPercentage().toString())
    }

    @Test
    fun toString1() {
        assertEquals("1.0%", 1.percent.toString(1))
        assertEquals("85.0%", 85.percent.toString(1))
        assertEquals("11.0%", 0.11.toPercentage().toString(1))
    }

    @Test
    fun toPercentageString() {
        assertEquals("100.00%", 1.0.toPercentageString())
        assertEquals("85.00%", 0.85.toPercentageString())
        assertEquals("11.00%", 0.11.toPercentageString())
    }
}
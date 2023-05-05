package me.him188.ic.grade.common

import me.him188.ic.grade.common.numbers.roundToString
import kotlin.test.Test
import kotlin.test.assertEquals

class RoundTest {
    @Test
    fun canRound2() {
        if (currentPlatform() == Platform.JS) return
        assertEquals("123.45", 123.454.roundToString(2))
        assertEquals("123.46", 123.455.roundToString(2))
        assertEquals("0.46", 0.455.roundToString(2))
        assertEquals("0.33", 0.33.roundToString(2))
        assertEquals("0.00", 0.0.roundToString(2))
        assertEquals("0.075", 0.075.roundToString(3))
        assertEquals("0.00", 0.0.roundToString(2))
        assertEquals("-0.00", (-0.0).roundToString(2))
    }

    @Test
    fun canRound1() {
        if (currentPlatform() == Platform.JS) return
        assertEquals("123.5", 123.454.roundToString(1))
        assertEquals("123.5", 123.455.roundToString(1))
        assertEquals("0.5", 0.455.roundToString(1))
        assertEquals("0.3", 0.33.roundToString(1))
        assertEquals("0.0", 0.0.roundToString(1))
    }
}

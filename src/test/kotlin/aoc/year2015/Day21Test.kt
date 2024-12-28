package aoc.year2015

import aoc.year2015.Day21Problem
import aoc.year2015.RPGEntity
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class Day21Test {

    @Test
    fun test_example_day21() {
        val p = Day21Problem()
        p.commonParts()
        assertTrue(p.simulate(RPGEntity(8, 5, 5), RPGEntity(12, 7, 2)))
        assertFalse(p.simulate(RPGEntity(8, 5, 5), RPGEntity(14, 7, 2)))
    }
}
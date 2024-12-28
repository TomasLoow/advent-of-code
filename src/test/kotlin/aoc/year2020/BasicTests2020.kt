package aoc.year2020

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2020 {

    @Test
    fun testDay1() {
        val problem = Day1Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(514579, problem.part1())
        assertEquals(241861950, problem.part2())
    }

    @Test
    fun testDay2() {
        val problem = Day2Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(2, problem.part1())
        assertEquals(1, problem.part2())
    }
    @Test
    fun testDay5() {
        val problem = Day5Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(820, problem.part1())
    }

    @Test
    fun testDay24() {
        val problem = Day24Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(10, problem.part1())
        assertEquals(2208, problem.part2())
    }

}

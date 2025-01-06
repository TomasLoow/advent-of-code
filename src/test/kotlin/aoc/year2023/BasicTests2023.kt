package aoc.year2023

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2023 {
    @Test
    fun testDay1() {
        val problem = Day01Problem()
        problem.testData = true
        problem.commonParts()
        // Different test data for part1 and part2. We skip testing part1.
        assertEquals(281, problem.part2())
        problem.getInputFile()
    }

    @Test
    fun testDay2() {
        val problem = Day02Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(8, problem.part1())
        assertEquals(2286, problem.part2())
        problem.getInputFile()
    }
    @Test
    fun testDay15() {
        val problem = Day15Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(1320, problem.part1())
        assertEquals(145, problem.part2())
        problem.getInputFile()
    }
}

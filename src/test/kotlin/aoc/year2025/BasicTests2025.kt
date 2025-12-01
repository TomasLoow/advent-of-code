package aoc.year2025

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2025 {
    @Test
    fun testDay1() {
        val problem = Day01Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(3, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(6, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
}
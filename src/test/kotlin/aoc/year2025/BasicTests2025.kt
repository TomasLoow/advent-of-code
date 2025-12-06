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

    @Test
    fun testDay2() {
        val problem = Day02Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(1227775554.toBigInteger(), problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(4174379265.toBigInteger(), problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay3() {
        val problem = Day03Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(357L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(3121910778619L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay4() {
        val problem = Day04Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(13, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(43, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay5() {
        val problem = Day05Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(3L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(14L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay6() {
        val problem = Day06Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(4277556.toBigInteger(), problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(3263827.toBigInteger(), problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

}
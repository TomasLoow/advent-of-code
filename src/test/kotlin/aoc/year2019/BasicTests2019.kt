package aoc.year2019

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2019 {

    @Test
    fun testDay1() {
        val problem = Day1Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(33583+654+2+2, problem.part1())
        assertEquals(50346+966+2+2, problem.part2())
    }

    @Test
    fun testDay3() {
        val problem = Day3Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(159, problem.part1())
        assertEquals(610, problem.part2())
    }
}

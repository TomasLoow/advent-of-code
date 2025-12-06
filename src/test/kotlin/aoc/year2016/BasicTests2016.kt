package aoc.year2016

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2016 {

    @Test
    fun testDay1() {
        val problem = Day01Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(8, problem.part1())
        assertEquals(4, problem.part2())
    }

    @Test
    fun testDay20() {
        val problem = Day20Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(3L, problem.part1())
        assertEquals(4294967288, problem.part2())
    }
}

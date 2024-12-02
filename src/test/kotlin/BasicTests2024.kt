import aoc.year2024.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class BasicTests2024 {
    @Test
    fun testDay1() {
        val problem = Day1Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(11, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(31, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay2() {
        val problem = Day2Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(2, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(4, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
}
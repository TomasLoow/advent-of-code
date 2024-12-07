import aoc.year2024.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

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
    @Test
    fun testDay3() {
        val problem = Day3Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(161, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(48, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay4() {
        val problem = Day4Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(18, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(9, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay5() {
        val problem = Day5Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(143, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(123, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay6() {
        val problem = Day6Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(41, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(6, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay7() {
        val problem = Day7Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(3749, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(11387L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
}
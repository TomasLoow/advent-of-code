import aoc.year2015.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BasicTests2015 {
    @Test
    fun testDay1() {
        val problem = Day1Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(2, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(4, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay2() {
        val problem = Day2Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(101, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(48, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay3() {
        val problem = Day3Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(4, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(3, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay4() {
        val problem = Day4Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(609043, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(6742839, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay5() {
        val problem = Day5Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(1, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(2, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
}
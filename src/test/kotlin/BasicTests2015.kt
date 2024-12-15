import aoc.year2015.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

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
    @Test
    fun testDay6() {
        val problem = Day6Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(3*500*500, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(1*500*500 + 1*500*500 + 2*500*500 + 3*500*500, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay7() {
        val problem = Day7Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(72, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(72, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay8() {
        val problem = Day8Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(12, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(19, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay9() {
        val problem = Day9Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(605, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(982, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay10() {
        val problem = Day10Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(418016, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(5924894, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay11() {
        val problem = Day11Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals("ghjaabcc", problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals("ghjbbcdd", problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay12() {
        val problem = Day12Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(6, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(4, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay13() {
        val problem = Day13Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(330, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(286, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay14() {
        val problem = Day14Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(2660, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(1564, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay15() {
        val problem = Day15Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(62842880, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(57600000, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }

    /* There was no test input for Day 16 */

    @Test
    fun testDay17() {
        val problem = Day17Problem().apply { testData = true }.apply { testData = true }
        problem.target = 25
        problem.commonParts()
        assertEquals(4, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(3, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }

    @Test
    fun testDay18() {
        val problem = Day18Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        // Problem on page used two different inputs. I use the one for part two here, the expected result for part 1 was verified by hand
        problem.steps = 4
        assertEquals(6, problem.part1(), "Correct Answer day ${problem.number } part 1")
        problem.steps = 5
        assertEquals(17, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay25() {
        val problem = Day25Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(21345942, problem.part1(), "Correct Answer day ${problem.number } part 1")
    }

}
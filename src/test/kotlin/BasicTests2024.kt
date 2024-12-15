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
    @Test
    fun testDay8() {
        val problem = Day8Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(14, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(34, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay9() {
        val problem = Day9Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(1928L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(2858L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay10() {
        val problem = Day10Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(36, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(81, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay11() {
        val problem = Day11Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(55312L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(65601038650482L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay12() {
        val problem = Day12Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(1930, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(1206, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay13() {
        val problem = Day13Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(480L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(875318608908L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay14() {
        val problem = Day14Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(12, problem.part1(), "Correct Answer day ${problem.number} part 1")
        // Not possible to do part 2 with test data.
    }
    @Test
    fun testDay15() {
        val problem = Day15Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(10092, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(9021, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
}
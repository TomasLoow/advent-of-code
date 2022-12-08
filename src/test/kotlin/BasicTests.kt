import aoc.year2022.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MultiTest {
    @Test
    fun testDay1() {
        val problem = Day1Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(24000, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(45000, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }

    @Test
    fun testDay2() {
        val problem = Day2Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(15, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(12, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay3() {
        val problem = Day3Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(157, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(70, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }

    @Test
    fun testDay4() {
        val problem = Day4Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(2, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(4, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay5() {
        val problem = Day5Problem().apply { testData = true }
        problem.commonParts()
        assertEquals("CMZ", problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals("MCD", problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay6() {
        val problem = Day6Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(11, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(26, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay7() {
        val problem = Day7Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(95437, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(24933642, problem.part2(),"Correct Answer day ${problem.number } part 2")
    }
    @Test
    fun testDay8() {
        val problem = Day8Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(21, problem.part1(), "Correct Answer day ${problem.number } part 1")
        assertEquals(8, problem.part2(), "Correct Answer day ${problem.number } part 2")
    }
}
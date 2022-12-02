import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MultiTest {


    @Test
    fun testDay1() {
        val problem = aoc2022.Day1Problem("input/aoc2022/testinput/day1.txt")
        problem.commonParts()
        assertEquals(24000, problem.part1())
        assertEquals(45000, problem.part2())
    }

    @Test
    fun testDay2() {
        val problem = aoc2022.Day2Problem("input/aoc2022/testinput/day2.txt")
        problem.commonParts()
        assertEquals(15, problem.part1())
        assertEquals(12, problem.part2())
    }
}
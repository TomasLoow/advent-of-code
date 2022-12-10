import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BasicTests2021 {
    @Test
    fun testDay1() {
        val problem = aoc.year2021.Day1Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(7, problem.part1())
        assertEquals(5, problem.part2())
    }

    @Test
    fun testDay2() {
        val problem = aoc.year2021.Day2Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(150, problem.part1())
        assertEquals(900, problem.part2())
    }

    @Test
    fun testDay3() {
        val problem = aoc.year2021.Day3Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(198, problem.part1())
        assertEquals(230, problem.part2())
    }

    @Test
    fun testDay4() {
        val problem = aoc.year2021.Day4Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(4512, problem.part1())
        assertEquals(1924, problem.part2())
    }

    @Test
    fun testDay5() {
        val problem = aoc.year2021.Day5Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(5, problem.part1())
        assertEquals(12, problem.part2())
    }

    @Test
    fun testDay6() {
        val problem = aoc.year2021.Day6Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(5934, problem.part1())
        assertEquals(26984457539, problem.part2())
    }

    @Test
    fun testDay7() {
        val problem = aoc.year2021.Day7Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(37, problem.part1())
        assertEquals(168, problem.part2())
    }

    @Test
    fun testDay8() {
        val problem = aoc.year2021.Day8Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(26, problem.part1())
        assertEquals(61229, problem.part2())
    }

    @Test
    fun testDay9() {
        val problem = aoc.year2021.Day9Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(15, problem.part1())
        assertEquals(1134, problem.part2())
    }

    @Test
    fun testDay10() {
        val problem = aoc.year2021.Day10Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(26397, problem.part1())
        assertEquals(288957, problem.part2())
    }

    @Test
    fun testDay11() {
        val problem = aoc.year2021.Day11Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(1656, problem.part1())
        assertEquals(195, problem.part2())
    }

    @Test
    fun testDay12() {
        val problem = aoc.year2021.Day12Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(10, problem.part1())
        assertEquals(36, problem.part2())
    }

    @Test
    fun testDay13() {
        val problem = aoc.year2021.Day13Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(17, problem.part1())
        problem.part2()
        val expected = listOf<String>(
            "#####",
            "#...#",
            "#...#",
            "#...#",
            "#####",
        )
        assertEquals(problem.display, expected)
    }

    @Test
    fun testDay14() {
        val problem = aoc.year2021.Day14Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(1588, problem.part1())
        assertEquals(2188189693529, problem.part2())
    }

    @Test
    fun testDay15() {
        val problem = aoc.year2021.Day15Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(40, problem.part1())
        assertEquals(315, problem.part2())
    }

    @Test
    fun testDay16() {
        val problem = aoc.year2021.Day16Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(20, problem.part1())
        assertEquals(1, problem.part2())
    }

    @Test
    fun testDay17() {
        val problem = aoc.year2021.Day17Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(45, problem.part1())
        assertEquals(112, problem.part2())
    }

    @Test
    fun testDay18() {
        val problem = aoc.year2021.Day18Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(4140, problem.part1())
        assertEquals(3993, problem.part2())
    }

    @Test
    fun testDay21() {
        val problem = aoc.year2021.Day21Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(739785, problem.part1())
        assertEquals(444356092776315, problem.part2())
    }

    @Test
    fun testDay22() {
        val problem = aoc.year2021.Day22Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(474140, problem.part1())
        assertEquals(2758514936282235, problem.part2())
    }

    @Test
    fun testDay25() {
        val problem = aoc.year2021.Day25Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(58, problem.part1())
        assertEquals(-1, problem.part2())
    }

}

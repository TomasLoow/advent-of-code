import aoc.year2022.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class BasicTests2022 {
    @Test
    fun testDay1() {
        val problem = Day1Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(24000, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(45000, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay2() {
        val problem = Day2Problem().apply { testData = true }.apply { testData = true }
        problem.commonParts()
        assertEquals(15, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(12, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay3() {
        val problem = Day3Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(157, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(70, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay4() {
        val problem = Day4Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(2, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(4, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay5() {
        val problem = Day5Problem().apply { testData = true }
        problem.commonParts()
        assertEquals("CMZ", problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals("MCD", problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay6() {
        val problem = Day6Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(11, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(26, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay7() {
        val problem = Day7Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(95437, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(24933642, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay8() {
        val problem = Day8Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(21, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(8, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay9() {
        val problem = Day9Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(88, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(36, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay10() {
        val expectedPart2 = """
            ██  ██  ██  ██  ██  ██  ██  ██  ██  ██  
            ███   ███   ███   ███   ███   ███   ███ 
            ████    ████    ████    ████    ████    
            █████     █████     █████     █████     
            ██████      ██████      ██████      ████
            ███████       ███████       ███████     
            
            """.trimIndent()
        val problem = Day10Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(13140, problem.part1(), "Correct Answer day ${problem.number} part 1")

        assertThrows(Exception::class.java) { problem.part2() }
        assertEquals(expectedPart2, problem.output, "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay11() {
        val problem = Day11Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(10605L, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(2713310158L, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay12() {
        val problem = Day12Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(31, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(29, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay13() {
        val problem = Day13Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(13, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(140, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay14() {
        val problem = Day14Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(24, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(93, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }

    @Test
    fun testDay15() {
        val problem = Day15Problem().apply { testData = true }
        problem.line = 10
        problem.size = 20
        problem.commonParts()
        assertEquals(26, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(56000011, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }


    @Test
    fun testDay16() {
        val problem = Day16Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(1651, problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(1707, problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
    @Test
    fun testDay17() {
        val problem = Day17Problem().apply { testData = true }
        problem.commonParts()
        assertEquals(3068.toBigInteger(), problem.part1(), "Correct Answer day ${problem.number} part 1")
        assertEquals(1514285714288.toBigInteger(), problem.part2(), "Correct Answer day ${problem.number} part 2")
    }
}
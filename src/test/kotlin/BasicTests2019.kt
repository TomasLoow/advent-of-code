import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class BasicTests2019 {

    @Test
    fun testDay1() {
        val problem = aoc.year2019.Day1Problem()
        problem.testData = true
        problem.commonParts()
        assertEquals(33583+654+2+2, problem.part1())
        assertEquals(50346+966+2+2, problem.part2())
    }
}

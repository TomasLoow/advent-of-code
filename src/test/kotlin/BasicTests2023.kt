import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

class BasicTests2023 {
    @Test
    fun testDay1() {
        val problem = aoc.year2023.Day1Problem()
        problem.testData = true
        problem.commonParts()
        // Different test data for part1 and part2. We skip testing part1.
        assertEquals(281, problem.part2())
        problem.getInputFile()
    }
}

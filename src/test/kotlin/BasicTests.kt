import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class MultiTest {
    @ParameterizedTest(name = "Example problem from Day {0}, should have answer {2}")
    @MethodSource("testCases")
    fun `The solution of part one for each day should satisfy the small example on the webpage`(
        day: Int,
        problem: DailyProblem,
        expected_1: Long,
        _expected_2: Long
    ) {
        problem.commonParts()
        assertEquals(expected_1, problem.part1(), "Solution for Day $day part 1 should be correct")
    }

    @ParameterizedTest(name = "Example problem from Day {0}, part 2 should have answer {3}")
    @MethodSource("testCases")
    fun `The solution of part two for each day should satisfy the small example on the webpage`(
        day: Int,
        problem: DailyProblem,
        _expected_1: Long,
        expected_2: Long
    ) {
        problem.commonParts()
        assertEquals(expected_2, problem.part2(), "Solution for Day $day part 2 should be correct")
    }

    private companion object {
        @JvmStatic
        fun testCases() = Stream.of(
            Arguments.of(1,  aoc2022.Day1Problem("input/aoc2022/testinput/day1.txt"), 24000, 45000),
        )
    }

}
package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day03Problem : DailyProblem<Long>() {

    override val number = 3
    override val year = 2025
    override val name = "Lobby"

    private lateinit var data: List<IntArray>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { line -> line.map { c -> c.toString().toInt() }.toIntArray() }
    }


    override fun part1(): Long {
        return data.sumOf {
            memo = Array(it.size*16 +1, { -1L })
            voltage2(it, 2, 0)
        }
    }

    override fun part2(): Long {
        return data.sumOf {
            memo = Array(it.size*16 + 1, { -1L })
            voltage2(it, 12, 0)
        }
    }

    private var memo = arrayOf<Long>()  // Simple memoization
    private fun voltage2(line: IntArray, digits: Int, start: Int): Long {
        val key = digits or (start shl 4)
        if (memo[key] != -1L) { return memo[key] }

        if (digits == 1) {
            val res = (start..<line.size).maxOf { line[it].toLong() }
            memo[key] = res
            return res
        }

        var res = 0L
        for (i in (start..(line.size - digits))) {
            val v = pow10(digits - 1) * line[i] + voltage2(line, digits - 1, i + 1)
            res = maxOf(res, v)
        }
        memo[key] = res
        return res
    }

}

val sillyPrecalc = arrayOf(
    1,
    10L,
    100L,
    1000L,
    10000L,
    100000L,
    1000000L,
    10000000L,
    100000000L,
    1000000000L,
    10000000000L,
    100000000000L
)

private fun pow10(p: Int): Long {
    return sillyPrecalc[p]
}

val day03Problem = Day03Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day03Problem.testData = false
    day03Problem.runBoth(100)
}
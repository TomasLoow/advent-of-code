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
        val lines = getInputText()
            .nonEmptyLines()
        val lineLength = lines.first().length
        data = lines
            .map { line ->
                IntArray(lineLength) { i -> line[i].digitToInt() }
            }
    }

    override fun part1(): Long {
        return data.sumOf {
            voltage(it, 2, 0)
        }
    }

    override fun part2(): Long {
        return data.sumOf {
            voltage(it, 12, 0)
        }
    }

    private fun voltage(line: IntArray, digits: Int, start: Int): Long {
        if (digits == 0) return 0

        val idxMax = (start..(line.size - digits)).maxBy { line[it] }
        val maxDigit = line[idxMax]

        val tail = voltage(line, digits - 1, idxMax + 1)
        return pow10(digits - 1) * maxDigit + tail
    }
}

private val sillyPrecalc = arrayOf(
          1L,
         10L,
         100L,
        1000L,          // Look! A Christmas tree!
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
    day03Problem.runBoth(500)
}
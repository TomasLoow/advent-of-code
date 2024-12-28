package aoc.year2020

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime
import aoc.utils.extensionFunctions.toInt

class Day5Problem : DailyProblem<Int>() {

    override val number = 5
    override val year = 2020
    override val name = "Binary Boarding"

    private lateinit var data: List<String>
    private lateinit var seatNumbers: List<Int>

    override fun commonParts() {
        data = getInputText().nonEmptyLines()
        seatNumbers = data.map {
            val fb = it.take(7).map { c -> c == 'B' }.toInt()
            val rl = it.drop(7).map { c -> c == 'R' }.toInt()
            (fb * 8) + rl
        }
    }


    override fun part1(): Int {
        return seatNumbers.max()
    }

    override fun part2(): Int {
        val arr = Array(8*128) { false}
        seatNumbers.forEach { arr[it] = true }

        var inUnusedSection = true
        arr.forEachIndexed { seatNumber, taken ->
            if (!taken) {
                if (!inUnusedSection) {
                    return seatNumber
                }
            } else {
                inUnusedSection = false
            }
        }
        throw Exception("No solution")
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.testData = false
    day5Problem.runBoth(100)
}
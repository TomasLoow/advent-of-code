package aoc.year2020

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {

    override val number = 1
    override val year = 2020
    override val name = "Report Repair"

    private lateinit var data: List<Int>

    override fun commonParts() {
        data = parseIntLines(getInputText()).sorted()

    }

    override fun part1(): Int {
        return findTwoNumbersThatSumTo(2020)
    }

    private fun findTwoNumbersThatSumTo(target: Int) : Int {
        data.forEach { n ->
            if (data.contains(target - n)) {
                return n * (target - n)
            }
        }
        throw Exception("No solution found")
    }

    override fun part2(): Int {
        for (n in data) {
            try {
                return n*findTwoNumbersThatSumTo(2020 - n)
            } catch (e: Exception) {
                continue
            }
        }
        throw Exception("No solution found")
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.testData = false
    day01Problem.runBoth(10)
}
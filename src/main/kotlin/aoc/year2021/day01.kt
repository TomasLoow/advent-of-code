package aoc.year2021

import aoc.DailyProblem
import aoc.utils.extensionFunctions.countIncreases
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime


class Day01Problem : DailyProblem<Long>() {

    override val number = 1
    override val year = 2021
    override val name = "Sonar Sweep"

    private lateinit var data: List<Int>

    private fun Collection<Int>.threeElementWindowSums(): Collection<Int> {
        return windowed(3)
            .map { it.sum() }
    }

    override fun commonParts() {
        data = parseIntLines(getInputText())

    }

    override fun part1(): Long {
        return data.countIncreases()
    }

    override fun part2(): Long {
        return data
            .threeElementWindowSums()
            .countIncreases()
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.runBoth(10)
}



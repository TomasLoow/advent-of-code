package aoc.year2021

import DailyProblem
import aoc.utils.shiftLeft
import kotlin.time.ExperimentalTime

class Day6Problem() : DailyProblem<Long>() {
    override val number = 6
    override val year = 2021
    override val name = "Lanternfish"

    private val stepsPart1 = 80
    private val stepsPart2 = 256


    fun parseFishesInput(): Array<Long> {
        val fishesCounts = Array<Long>(9) { 0 }

        getInputFile().readLines()
            .first()
            .split(",")
            .map { it.toInt() }
            .forEach { fishValue ->
                fishesCounts[fishValue]++
            }

        return fishesCounts
    }

    private fun simulate(fishesCounts: Array<Long>, steps: Int): Long {
        for (step in 1..steps) {
            val zeroes = fishesCounts.shiftLeft(0)
            fishesCounts[6] += zeroes // Old zeroes are back to countdown 6
            fishesCounts[8] = zeroes  // Their babies
        }
        return fishesCounts.sum()
    }


    override fun part1(): Long {
        val fishesCounts = parseFishesInput()
        return simulate(fishesCounts, stepsPart1)
    }

    override fun part2(): Long {
        val fishesCounts = parseFishesInput()
        return simulate(fishesCounts, stepsPart2)
    }
}

val day6Problem = Day6Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day6Problem.runBoth(1000)
}

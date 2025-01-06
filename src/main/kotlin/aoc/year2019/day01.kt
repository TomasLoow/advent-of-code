package aoc.year2019

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {

    override val number = 1
    override val year = 2019
    override val name = "The Tyranny of the Rocket Equation"

    private lateinit var masses: List<Int>

    override fun commonParts() {
        masses = parseIntLines(getInputText())
    }

    override fun part1(): Int {
        return masses.sumOf { it / 3 - 2 }
    }

    private fun fuelNeeded(mass: Int): Int {
        val fuelForMass = mass / 3 - 2
        if (fuelForMass < 0) return 0

        return fuelForMass + fuelNeeded(fuelForMass)
    }
    override fun part2(): Int {
        return masses.sumOf { fuelNeeded(it) }
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.testData = false
    day01Problem.runBoth(10)
}
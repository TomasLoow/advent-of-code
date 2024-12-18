package aoc.year2019

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day1Problem : DailyProblem<Int>() {

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

    fun fuelNeeded(mass: Int): Int {
        val fuelForMass = mass / 3 - 2
        if (fuelForMass < 0) return 0

        return fuelForMass + fuelNeeded(fuelForMass)
    }
    override fun part2(): Int {
        return masses.sumOf { fuelNeeded(it) }
    }
}

val day1Problem = Day1Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day1Problem.testData = false
    day1Problem.runBoth(10)
}
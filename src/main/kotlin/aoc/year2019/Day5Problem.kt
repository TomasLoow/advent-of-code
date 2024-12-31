package aoc.year2019

import DailyProblem
import aoc.utils.parseIntCodeComputer
import kotlin.time.ExperimentalTime

class Day5Problem : DailyProblem<Long>() {

    override val number = 5
    override val year = 2019
    override val name = "Sunny with a Chance of Asteroids"

    private lateinit var computer: IntCode

    override fun commonParts() {
        computer = parseIntCodeComputer(getInputText())
    }

    override fun part1(): Long {
        computer.reset()

        val output = computer.runUntilHalt(listOf(1))
        return output.last()
    }

    override fun part2(): Long {
        computer.reset()

        val output = computer.runUntilHalt(listOf(5))
        return output.first()
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.testData = false
    day5Problem.runBoth(100)
}
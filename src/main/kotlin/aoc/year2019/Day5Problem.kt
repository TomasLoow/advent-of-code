package aoc.year2019

import DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day5Problem : DailyProblem<Int>() {

    override val number = 5
    override val year = 2019
    override val name = "Sunny with a Chance of Asteroids"

    private lateinit var initMemory: Array<Int>

    override fun commonParts() {
        initMemory = parseOneLineOfSeparated(getInputText().replace("\n", "").nonEmptyLines().first(), String::toInt, ",").toTypedArray()
    }

    override fun part1(): Int {
        val memory = initMemory.copyOf()
        val computer = IntCode(memory)

        val output = computer.runFully(sequenceOf(1))
        return output.last()
    }

    override fun part2(): Int {
        val memory = initMemory.copyOf()
        val computer = IntCode(memory)

        val output = computer.runFully(sequenceOf(5))
        return output.first()
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.testData = false
    day5Problem.runBoth(100)
}
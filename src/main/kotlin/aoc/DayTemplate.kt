package aoc

import DailyProblem
import kotlin.time.ExperimentalTime

class Day999999Problem : DailyProblem<Long>() {

    override val number = 999999
    override val year = 2024
    override val name = "Problem name"

    private lateinit var data: Any

    override fun commonParts() {
        data = getInputText()
    }


    override fun part1(): Long {
        return 1
    }


    override fun part2(): Long {
        return 1
    }
}

val day999999Problem = Day999999Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day999999Problem.testData = true
    day999999Problem.runBoth(1)
}
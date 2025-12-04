package aoc

import aoc.DailyProblem
import kotlin.time.ExperimentalTime

class Day999999Problem : DailyProblem<Long>() {

    override val number = 999999
    override val year = 2025
    override val name = "Problem name"

    private lateinit var data: Any

    override fun commonParts() {
        data = getInputText()
    }


    override fun part1(): Long {
        return 999999
    }


    override fun part2(): Long {
        return 999999
    }
}

val day999999Problem = Day999999Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day999999Problem.testData = true
    day999999Problem.runBoth(1)
}
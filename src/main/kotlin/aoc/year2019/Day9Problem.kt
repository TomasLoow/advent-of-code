package aoc.year2019

import DailyProblem
import aoc.utils.parseIntCodeComputer
import kotlin.time.ExperimentalTime

class Day9Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2019
    override val name = "Sensor Boost"

    private lateinit var c: IntCode

    override fun commonParts() {
        c= parseIntCodeComputer(getInputText())
    }


    override fun part1(): Long {
        c.reset()
        c.writeInput(1)
        val res = c.runUntilHalt()
        println(res)
        return res.last()
    }


    override fun part2(): Long {
        c.reset()
        c.writeInput(2)
        val res = c.runUntilHalt()
        println(res)
        return res.last()
    }
}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.testData = false
    day9Problem.runBoth(1)
}
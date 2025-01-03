package aoc.year2023

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseOneLineOfSeparated
import kotlin.time.ExperimentalTime

class Day9Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2023
    override val name = "Mirage Maintenance"

    private lateinit var lines: List<List<Long>>

    override fun commonParts() {
        lines = getInputText().nonEmptyLines().map { line -> parseOneLineOfSeparated(line, String::toLong, " ") }
    }

    fun solve(line: List<Long>): Long {
        val lines = mutableListOf(line)
        while (! lines.last().all { it == 0L }) {
            lines.add(lines.last().zipWithNext { a, b -> b - a })
        }
        return lines.map { it.last() }.sum()
    }

    override fun part1(): Long {
        return lines.sumOf { solve(it) }
    }


    override fun part2(): Long {
        return lines.sumOf { solve(it.reversed()) }
    }
}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.testData = false
    day9Problem.runBoth(100)
}
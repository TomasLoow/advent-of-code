package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseOneLineOfSeparated
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime


class Day2Problem : DailyProblem<Int>() {

    override val number = 2
    override val year = 2024
    override val name = "Red-Nosed Reports "

    private lateinit var lines: List<List<Int>>

    override fun commonParts() {
        lines = getInputText().nonEmptyLines().map {
            parseOneLineOfSeparated(it, ::parseInt, " ")
        }
    }

    override fun part1(): Int {
        val valid = lines.filter { line ->
            validLine(line)
        }
        return valid.size
    }


    override fun part2(): Int {
        val valid = lines.filter { line ->
            validLine(line) || dropOneFrom(line).any(::validLine)
        }
        return valid.size
    }

    private fun dropOneFrom(line: List<Int>): List<List<Int>> {
        return buildList {
            (0..line.size - 1).forEach { i ->
                add(line.take(i) + line.drop(i + 1))
            }
        }
    }

    private fun validLine(line: List<Int>): Boolean {
        val diffs = line.zipWithNext { a, b -> b - a }
        return diffs.all { it in (1..3) } || diffs.all { it in (-3..-1) }
    }
}

val day2Problem = Day2Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day2Problem.runBoth(timesToRun = 100)
}


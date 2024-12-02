package aoc.year2024

import DailyProblem
import aoc.utils.*
import java.lang.Integer.parseInt
import java.util.stream.Stream
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime


private fun List<Int>.isAscending(): Boolean {
    return this.windowed(2).all { it[0] <= it[1] }
}

private fun List<Int>.isDescending(): Boolean {
    return this.windowed(2).all { it[0] >= it[1] }
}

private fun List<Int>.stepsBetweenOneAndThree() = this.windowed(2).all {
    val diff = (it[0] - it[1]).absoluteValue
    diff >= 1 && diff <= 3
}


class Day2Problem() : DailyProblem<Int>() {

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

    private fun validLine(line: List<Int>) =
        (line.isAscending() || line.isDescending()) && line.stepsBetweenOneAndThree()
}

val day2Problem = Day2Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day2Problem.runBoth(timesToRun = 100)
}


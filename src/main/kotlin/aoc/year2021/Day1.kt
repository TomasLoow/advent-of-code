package aoc.year2021

import DailyProblem
import java.io.File
import kotlin.time.ExperimentalTime

fun parseIntsFile(path: String): List<Int> {
    val lines: List<String> = File(path).readLines()
    return lines.map { it.toInt() }
}


private fun Collection<Int>.countIncreases(): Long {
    return windowed(2).count { it[0] < it[1] }.toLong()
}


private fun Collection<Int>.threeElementWindowSums(): Collection<Int> {
    return windowed(3)
        .map { it.sum() }
}

class Day1Problem() : DailyProblem<Long>() {

    override val number = 1
    override val year = 2021
    override val name = "Sonar Sweep"

    override fun part1(): Long {
        return parseIntsFile(this.getInputFile().absolutePath)
            .countIncreases()
    }
    override fun part2(): Long {
        return parseIntsFile(this.getInputFile().absolutePath)
            .threeElementWindowSums()
            .countIncreases()
    }
}

val day1Problem = Day1Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day1Problem.runBoth(10)
}



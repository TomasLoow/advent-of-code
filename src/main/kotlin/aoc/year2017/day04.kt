package aoc.year2017

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day04Problem : DailyProblem<Long>() {

    override val number = 4
    override val year = 2017
    override val name = "High-Entropy Passphrases"

    private lateinit var lines : List<List<String>>

    override fun commonParts() {
        lines = getInputText().nonEmptyLines().map { line -> line.split(" ") }
    }

    override fun part1(): Long {
        return lines.count { line ->
            line.distinct().size == line.size
        }.toLong()
    }

    override fun part2(): Long {
        return lines.map { line -> line.map { it.sorted() } }.count { line ->
            line.distinct().size == line.size
        }.toLong()
    }
}

fun String.sorted(): String {
    return this.toList().sorted().toString()
}

val day04Problem = Day04Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day04Problem.testData = false
    day04Problem.runBoth(100)
}
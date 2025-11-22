package aoc.year2017

import aoc.DailyProblem
import aoc.utils.extensionFunctions.allPairs
import aoc.utils.extensionFunctions.minAndMax
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseOneLineOfSeparated
import kotlin.time.ExperimentalTime

class Day02Problem : DailyProblem<Long>() {

    override val number = 2
    override val year = 2017
    override val name = "Corruption Checksum"

    private lateinit var data: List<List<Int>>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { parseOneLineOfSeparated(it,String::toInt, Regex(" +|\t+")) }
    }

    override fun part1(): Long {
        return data.sumOf {
            val (lo,hi) = it.minAndMax()
            hi - lo
        }.toLong()
    }

    override fun part2(): Long {
        return data.sumOf { line ->
            line.allPairs().first { (a,b) -> a % b == 0 }.let { (a,b) -> a /b }
        }.toLong()
    }
}

val day02Problem = Day02Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day02Problem.testData = false
    day02Problem.runBoth(1)
}
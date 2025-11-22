package aoc.year2015

import aoc.DailyProblem
import aoc.utils.extensionFunctions.allSubsetsWithSum
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day17Problem : DailyProblem<Int>() {

    override val number = 17
    override val year = 2015
    override val name = "No Such Thing as Too Much"

    var target: Int = 150

    private lateinit var data: List<Int>
    private lateinit var combinations: List<List<Int>>

    override fun commonParts() {
        data = parseIntLines(getInputText())
        combinations = data.allSubsetsWithSum(target)
    }


    override fun part1(): Int {
        return combinations.size
    }


    override fun part2(): Int {
        val minSize = combinations.minOf { it.size }
        return combinations.count { it.size == minSize }
    }
}

val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.runBoth(100)
}

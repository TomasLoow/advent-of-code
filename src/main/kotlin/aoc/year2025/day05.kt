package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.totalLengthOfCovered
import aoc.utils.parseLongLines
import aoc.utils.parseTwoBlocks
import kotlin.time.ExperimentalTime

class Day05Problem : DailyProblem<Long>() {

    override val number = 5
    override val year = 2025
    override val name = "Cafeteria"

    private lateinit var ranges: List<LongRange>
    private lateinit var ingredients: List<Long>

    override fun commonParts() {
        parseTwoBlocks(
            getInputText(),
            {
                it.lines().map {
                    it.split("-").let { (a, b) -> a.toLong()..b.toLong() }
                }
            },
            { parseLongLines(it) }
        ).let { (x, y) ->
            ranges = x
            ingredients = y

        }
    }

    override fun part1(): Long {
        return ingredients.count { i -> ranges.any { r -> i in r } }.toLong()
    }

    override fun part2(): Long {
        return ranges.totalLengthOfCovered()
    }
}

val day05Problem = Day05Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day05Problem.testData = false
    day05Problem.runBoth(100)

}
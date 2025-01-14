package aoc.year2017

import DailyProblem
import kotlin.math.absoluteValue
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Coord

class Day03Problem : DailyProblem<Long>() {

    override val number = 3
    override val year = 2017
    override val name = "Spiral Memory"

    private var target by Delegates.notNull<Int>()

    override fun commonParts() {
        target = getInputText().nonEmptyLines().single().toInt()
    }

    fun findOddSquareGt(target: Int): Int {
        var i = 1
        while (i * i < target) {
            i += 2
        }
        return i
    }


    override fun part1(): Long {
        val sqGt = findOddSquareGt(target)
        val numbersAround = 4 * sqGt - 4
        val squared = sqGt * sqGt
        val side = numbersAround / 4
        val corners = listOf(squared, squared - side, squared - 2* side, squared - 3* side)

        val distToCorner = corners.minOf{(target - it).absoluteValue }
        return ((sqGt - 1) - distToCorner).toLong()
    }

    override fun part2(): Long {
        return 1L
    }
}

val day03Problem = Day03Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day03Problem.testData = false
    day03Problem.runBoth(1)
}
package aoc.year2015

import DailyProblem
import aoc.utils.parseAllDigits
import kotlin.time.ExperimentalTime

class Day25Problem : DailyProblem<Long>() {

    override val number = 25
    override val year = 2015
    override val name = "Let It Snow"

    private var targetCol = 0
    private var targetRow = 0

    override fun commonParts() {
        if (testData) {
            targetCol = 3
            targetRow = 4
            return
        }
        val digits = parseAllDigits(getInputText())
        targetCol = digits[1]
        targetRow = digits[0]
    }

    private fun findIndexAtCoordinate(col: Int, row: Int): Int {
        // Note that the top row contains the triangular numbers.
        // To find the index, note what diagonal it's in, find the value in the top row and add to this depending on
        // how far down in the grid we are.
        val diagonalIndex = col + row - 1
        val topRowValue = (1..diagonalIndex).sum()  // Value at row 1 in this diagonal.
        return topRowValue - (row - 1)
    }

    override fun part1(): Long {
        fun next(res: Long) = res * 252533L % 33554393L

        val idx = findIndexAtCoordinate(targetCol, targetRow)
        var res = 20151125L
        repeat((idx - 1)) { res = next(res) }
        return res
    }


    override fun part2(): Long {
        // No part 2. Merry Christmas!
        return 0L
    }
}

val day25Problem = Day25Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day25Problem.runBoth(10)
}
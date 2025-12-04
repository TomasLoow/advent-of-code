package aoc.year2025

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import kotlin.time.ExperimentalTime

class Day04Problem : DailyProblem<Int>() {

    override val number = 4
    override val year = 2025
    override val name = "Printing Department"

    private lateinit var rollsMap: Array2D<Int>

    override fun commonParts() {
        rollsMap = Array2D.parseFromLines(getInputText()) { if (it == '@') 1 else 0}
    }


    override fun part1(): Int {
        val rolls = rollsMap.allCoords.filter { rollsMap[it] == 1 }.toMutableSet()

        val removable = findRemovable(rolls)
        return removable.count()
    }

    override fun part2(): Int {
        var countRemoved = 0
        val rolls = rollsMap.allCoords.filter { rollsMap[it] == 1 }.toMutableSet()
        while (true) {
            val removalble = findRemovable(rolls)
            if (removalble.isEmpty()) return countRemoved
            countRemoved += removalble.size
            rolls -= removalble
            rollsMap[removalble] = 0
        }
    }

    /* Returns the coordinates of all paper rolls that can be removed by the forklift */
    private fun findRemovable(candidates: Collection<Coord>): List<Coord> {
        return candidates.filter { rollsMap.neighbourValues(it, true).sum() < 4 }
    }
}

val day04Problem = Day04Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day04Problem.testData = false
    day04Problem.runBoth(50)
}
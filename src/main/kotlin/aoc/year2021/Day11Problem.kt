package aoc.year2021

import DailyProblem
import aoc.utils.Array2D
import aoc.utils.Coord
import aoc.utils.emptyMutableSet
import aoc.utils.parseIntArray
import kotlin.time.ExperimentalTime

private class OctopusGrid(val grid: Array2D<Int>) {

    /**
     * Runs a step of the simulation and returns the number of flashes that occurred
     */
    fun step(): Int {
        grid.modifyArea(grid.rect) { it + 1 }
        val flashCount = doFlashes()

        return flashCount
    }

    /**
     * Return true if all octopuses are 0
     */
    fun allZero(): Boolean {
        return grid.countIndexedByCoordinate { _, v -> v != 0 } == 0
    }

    private fun doFlashes(): Int {
        fun findCoordsThatNeedToFlash(coordsThatHaveFlashed: MutableSet<Coord>) = grid
            .mapAndFilterToListByNotNull { coord, value -> if (value > 9 && coord !in coordsThatHaveFlashed) coord else null }
            .toMutableList()

        val coordsThatHaveFlashedThisStep = emptyMutableSet<Coord>()
        var coordsToFlashQueue = findCoordsThatNeedToFlash(coordsThatHaveFlashedThisStep)
        do {
            var hasFlashedSomewhere = false
            if (coordsToFlashQueue.isEmpty()) {  // Find any new points that need to flash
                coordsToFlashQueue = findCoordsThatNeedToFlash(coordsThatHaveFlashedThisStep)
                if (coordsToFlashQueue.isEmpty()) continue  // No new points to flash found
            }
            val flashPoint = coordsToFlashQueue.first()
            coordsToFlashQueue.remove(flashPoint)
            doFlash(flashPoint)
            hasFlashedSomewhere = true
            coordsThatHaveFlashedThisStep.add(flashPoint)
        } while (hasFlashedSomewhere)

        coordsThatHaveFlashedThisStep.forEach { grid[it] = 0 }
        return coordsThatHaveFlashedThisStep.size
    }


    /**
     * Increases the value of all neighbours of a point
     */
    private fun doFlash(c: Coord) {
        grid.neighbourCoords(c, diagonal = true).forEach {
            grid[it]++
        }
    }
}

class Day11Problem : DailyProblem<Long>() {

    override val number = 11
    override val year = 2021
    override val name = "Dumbo Octopus"

    private fun parseGrid(): OctopusGrid {
        return OctopusGrid(parseIntArray(getInputText()))
    }

    override fun part1(): Long {
        val octopusGrid = parseGrid()
        val flashCount = (1..100).sumOf { octopusGrid.step() }
        return flashCount.toLong()
    }

    override fun part2(): Long {
        val octopusGrid = parseGrid()
        var step = 0L
        do {
            octopusGrid.step()
            step++
        } while (!octopusGrid.allZero())
        return step
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.runBoth(100)
}
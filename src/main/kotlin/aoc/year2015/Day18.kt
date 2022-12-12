package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day18Problem() : DailyProblem<Int>() {

    override val number = 18
    override val year = 2015
    override val name = "Like a GIF For Your Yard"
    private lateinit var startGrid: Array2D<Boolean>

    override fun commonParts() {
        startGrid = Array2D.parseFromLines(getInputText()) { it == '#'}
    }

    fun step(grid: Array2D<Boolean>, gridNext: Array2D<Boolean>) {
        gridNext.mapIndexed{ c, _ ->
            val value = grid[c]
            val neigboursOnCount = grid.neighbours(c).values.count { it }
            if (value) (neigboursOnCount == 2 || neigboursOnCount == 3) else (neigboursOnCount == 3)
        }
    }
    fun step2(grid: Array2D<Boolean>, gridNext: Array2D<Boolean>) {
        step(grid, gridNext)
        gridNext[Coord(0,0)] = true
        gridNext[Coord(0,gridNext.height-1)] = true
        gridNext[Coord(gridNext.width-1,gridNext.height-1)] = true
        gridNext[Coord(gridNext.width-1,0)] = true
    }

    override fun part1(): Int {
        var g1 = startGrid
        var g2 = Array2D(startGrid.width,startGrid.height,false)
        repeat(100) {
            step(g1, g2)
            val temp = g1
            g1 = g2
            g2 = temp
        }
        return g1.countIndexedByCoordinate { _, b -> b }
    }


    override fun part2(): Int {
        var g1 = startGrid
        var g2 = Array2D(startGrid.width,startGrid.height,false)
        repeat(100) {
            step2(g1, g2)
            val temp = g1
            g1 = g2
            g2 = temp
        }
        return g1.countIndexedByCoordinate { _, b -> b }
    }
}

val day18Problem = Day18Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day18Problem.runBoth(20)
}
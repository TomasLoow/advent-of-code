package aoc.year2021

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.parseCoord
import aoc.utils.parseListOfPairs


private class VentLine(private val start: Coord, private val end: Coord) {

    companion object {
        var gridSize: Int = 0
    }

    init {
        val biggest = listOf(start.x, start.y, end.x, end.y).maxOf { it }
        if (biggest > gridSize) {
            gridSize = biggest
        }
    }

    fun isStraight(): Boolean {
        return start.x == end.x || start.y == end.y
    }

    fun markCoveredPoints(arr: Array2D<Int>) {
        val delta = Pair(end.x.compareTo(start.x), end.y.compareTo(start.y))
        val distance: Int = start.chebyshevDistanceTo(end)

        var pos = start
        repeat(distance+1) {
            arr[pos]++
            pos += delta
        }
    }
}

private fun List<VentLine>.countIntersections(): Long {
    val size = VentLine.gridSize + 1
    val array: Array2D<Int> = Array2D(size, size, 0)
    forEach { it.markCoveredPoints(array) }
    return array.count { it > 1 }.toLong()
}

class Day05Problem : DailyProblem<Long>() {
    override val number = 5
    override val year = 2021
    override val name = "Hydrothermal Venture"

    private lateinit var lines: List<VentLine>

    private fun parseLinesFile(): List<VentLine> {
        return parseListOfPairs(getInputText(), ::parseCoord, ::parseCoord, separator = " -> ").map { (start, end) ->
            VentLine(start, end)
        }
    }

    override fun commonParts() {
        lines = parseLinesFile()
    }

    override fun part1(): Long {
        return lines
            .filter { it.isStraight() }
            .countIntersections()
    }

    override fun part2(): Long {
        return lines
            .countIntersections()
    }
}

val day05Problem = Day05Problem()

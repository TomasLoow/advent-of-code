package aoc.year2021

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime


class SeaFloor(private val map: Array2D<Int>) {
    operator fun get(c: Coord): Int = map[c]
    private fun isLowestPoint(p: Coord): Boolean {
        val here = map[p]
        val neighbourValues = map.neighbourValues(p, diagonal = false)
        return neighbourValues.all { it > here }
    }

    fun findLowestPoints(): List<Coord> {
        return map.allCoords.filter { isLowestPoint(it) }
    }

    private fun floodFill(p: Coord, basinPoints: MutableSet<Coord>) {
        if (basinPoints.contains(p)) return
        if (p !in map) return
        if (map[p] == 9) return

        basinPoints.add(p)
        map.neighbourCoords(p, diagonal = false).forEach { n ->
            floodFill(n, basinPoints)
        }
    }

    fun sizeOfBasinAt(p: Coord): Int {
        val basin = emptyMutableSet<Coord>()
        floodFill(p, basin)
        return basin.size
    }
}


class Day9Problem() : DailyProblem<Long>() {

    override val number = 9
    override val year = 2021

    override val name = "Smoke Basin"

    private lateinit var lowestPoints: List<Coord>
    private lateinit var seaFloor: SeaFloor

    override fun commonParts() {
        seaFloor = SeaFloor(parseIntArray(getInputText()))
        lowestPoints = seaFloor
            .findLowestPoints()
    }
    override fun part1(): Long {
        return lowestPoints
            .sumOf { seaFloor[it] + 1 }
            .toLong()
    }

    override fun part2(): Long {

        return lowestPoints
            .map { p ->
                seaFloor.sizeOfBasinAt(p)
            }
            .sortedDescending()
            .take(3)
            .product()
            .toLong()
    }
}


val day9Problem = Day9Problem()


@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.commonParts()
    day9Problem.runBoth(10)
}


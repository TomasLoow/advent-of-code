package aoc.year2024

import DailyProblem
import aoc.utils.*
import aoc.utils.algorithms.hasLoop
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Cursor
import aoc.utils.geometry.Direction
import kotlin.time.ExperimentalTime


class Day6Problem : DailyProblem<Int>() {
    override val number = 6
    override val year = 2024
    override val name = "Guard Gallivant"

    data class Guard(val cursor: Cursor<Boolean>, var dir: Direction)
    private lateinit var map: Array2D<Boolean>
    private lateinit var guardStartPos: Coord
    private lateinit var guardStartDir: Direction


    override fun commonParts() {
        val d = Array2D.parseFromLines(getInputText(), ::id)
        val (coord, dir) = d.findIndexedByCoordinate { _, c -> c in "v^<>" }!!
        guardStartPos = coord
        guardStartDir = parseDirectionFromArrow(dir)
        map = d.map { it == '#' }
    }

    private fun runPart1(): Set<Coord> {
        val guard = Guard(map.cursor(guardStartPos), guardStartDir)
        val visited = mutableSetOf(guard.cursor.coord)
        while (true) {
            val inside = guard.cursor.move(guard.dir)
            if (!inside) return visited
            if (guard.cursor.value) {
                guard.cursor.back()
                guard.dir = guard.dir.rotateCW()
            } else {
                visited.add(guard.cursor.coord)
            }
        }
    }

    override fun part1(): Int {
        val visited = runPart1()
        return visited.size
    }


    private fun runPart2(extraWall: Coord): Boolean {
        val s: Sequence<Pair<Coord, Direction>> = sequence {
            val guard = Guard(map.cursor(guardStartPos), guardStartDir)
            while (true) {
                val inside = guard.cursor.move(guard.dir)
                if (!inside) break
                if (guard.cursor.value || guard.cursor.coord == extraWall) {
                    guard.cursor.back()
                    guard.dir = guard.dir.rotateCW()
                } else {
                    yield(guard.cursor.coord to guard.dir)
                }
            }
        }
        return hasLoop(s)
    }

    override fun part2(): Int {
        val visitedNormally = runPart1()
        return visitedNormally.count { coord -> runPart2(coord) }
    }
}

val day6Problem = Day6Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day6Problem.runBoth(10)
}
package aoc.year2022

import DailyProblem
import aoc.utils.*
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime


class Day14Problem : DailyProblem<Int>() {

    override val number = 14
    override val year = 2022
    override val name = "Regolith Reservoir"

    private lateinit var inputLines: List<List<Coord>>
    private lateinit var map: Array2D<SandSpot>

    companion object {
        val SAND_SOURCE = Coord(500, 0)
    }

    enum class SandSpot {
        Empty, Wall, Sand
    }

    override fun commonParts() {
        inputLines = getInputText().nonEmptyLines().map { line ->
            line.split(" -> ").map(::parseCoord)
        }
    }

    private fun buildMap(width: Int, input: List<List<Coord>>) {
        map = Array2D(width, 500, SandSpot.Empty)
        input.forEach { line ->
            line.windowed(2) { (from, to) ->
                if (from.y == to.y) {
                    (min(from.x, to.x)..max(from.x, to.x)).forEach { x -> map[x, from.y] = SandSpot.Wall }
                } else {
                    (min(from.y, to.y)..max(from.y, to.y)).forEach { y -> map[from.x, y] = SandSpot.Wall }
                }
            }
        }
    }

    fun buildMapPart1() {
        val maxX = inputLines.maxOf { line -> line.maxOf { it.x } }
        buildMap(maxX + 1, inputLines)
    }

    fun buildMapPart2() {
        val lineY = inputLines.maxOf { line -> line.maxOf { it.y } } + 2


        val lines = inputLines + listOf(listOf(Coord(500 - lineY, lineY), Coord(500 + lineY, lineY)))
        val maxX = lines.maxOf { line -> line.maxOf { it.x } }
        buildMap(maxX + 1, lines)

    }


    private fun printMap() {
        map.print { s ->
            when (s) {
                SandSpot.Empty -> "."
                SandSpot.Wall -> "#"
                SandSpot.Sand -> "o"
            }
        }
    }

    fun fallSand1(): Boolean {
        try {
            var coord = SAND_SOURCE
            while (true) {
                val curs = map.cursor(coord)
                while (true) {
                    if (!curs.moveDown()) return false
                    if (curs.value != SandSpot.Empty) {
                        coord = curs.prev
                        break
                    }
                }
                val bl = coord.stepInDir(Direction.DOWN).stepInDir(Direction.LEFT)
                val br = coord.stepInDir(Direction.DOWN).stepInDir(Direction.RIGHT)
                if (map[bl] != SandSpot.Empty && map[br] != SandSpot.Empty) break
                if (map[bl] == SandSpot.Empty) coord = bl
                if (map[bl] != SandSpot.Empty && map[br] == SandSpot.Empty) coord = br
            }
            map[coord] = SandSpot.Sand
            return true
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
    }
    private fun part2BFS() : Int {
        val q = ArrayDeque<Coord>(1000)
        val visited = emptyMutableSet<Coord>()
        q.addLast(SAND_SOURCE)
        while (q.isNotEmpty()) {
            val c = q.removeFirst()
            if (c in visited) continue
            if (map[c] != SandSpot.Empty) continue

            visited.add(c)
            q.addLast(c.stepInDir(Direction.DOWNLEFT))
            q.addLast(c.stepInDir(Direction.DOWN))
            q.addLast(c.stepInDir(Direction.DOWNRIGHT))
        }
        return visited.size
    }


    override fun part1(): Int {
        buildMapPart1()

        var done = false
        var fallen = 0
        while (!done) {
            val landed = fallSand1()
            if (landed) fallen++ else done = true
        }
        return fallen
    }

    override fun part2(): Int {
        buildMapPart2()
        return part2BFS()
    }
}

val day14Problem = Day14Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day14Problem.testData = false
    day14Problem.runBoth(1000)
}
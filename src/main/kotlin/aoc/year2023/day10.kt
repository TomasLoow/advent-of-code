package aoc.year2023

import DailyProblem
import aoc.utils.algorithms.Djikstra
import aoc.utils.algorithms.DjikstraResult
import aoc.utils.extensionFunctions.odd
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.parseCharArray
import kotlin.time.ExperimentalTime


class PipeFinder(val map: Array2D<Char>) : Djikstra<Coord>() {
    override fun reachable(state: Coord): Collection<Coord> {
        return buildList {
            listOf(
                Triple(Direction.RIGHT, "-FLS", "-7J"),
                Triple(Direction.LEFT, "J7-S", "-FL"),
                Triple(Direction.UP, "|JLS", "|F7"),
                Triple(Direction.DOWN, "|7FS", "|LJ")
            ).forEach { (dir, exits, connections) ->
                val n = state + dir
                if (map[state]!! in exits && n in map && map[n] in connections) add(n)
            }
        }

    }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return 1
    }
}

class Day10Problem : DailyProblem<Long>() {

    override val number = 10
    override val year = 2023
    override val name = "Problem name"

    private lateinit var map: Array2D<Char>
    private lateinit var start: Coord
    private lateinit var djikstraResult: DjikstraResult<Coord>

    override fun commonParts() {
        map = parseCharArray(getInputText())
        start = map.findIndexedByCoordinate { c, v -> v == 'S' }!!.first
        val startChar = determineStartChar()
        map[start] = startChar
        val pf = PipeFinder(map)
        djikstraResult = pf.solveScoreForAllStates(start)
    }

    private fun determineStartChar(): Char {
        val connFromR = (start + Direction.RIGHT in map) && map[start + Direction.RIGHT]!! in "7J-"
        val connFromL = (start + Direction.LEFT in map) && map[start + Direction.LEFT]!! in "FL-"
        val connFromU = (start + Direction.UP in map) && map[start + Direction.UP]!! in "7F|"
        val connFromD = (start + Direction.DOWN in map) && map[start + Direction.DOWN]!! in "LJ|-"

        val startChar = if (connFromR && connFromL) '-'
        else if (connFromR && connFromD) 'F'
        else if (connFromR && connFromU) 'L'
        else if (connFromU && connFromD) '|'
        else if (connFromU && connFromL) 'J'
        else '7'
        return startChar
    }

    override fun part1(): Long {
        return djikstraResult.costs.maxOf { cost -> cost.value }.toLong()
    }


    override fun part2(): Long {
        val candidateCoords = mutableSetOf<Coord>()
        map.allCoords.forEach { coord ->
            if (coord !in djikstraResult.costs) {
                map[coord] = ' '
                candidateCoords += coord
            }
        }
        val outside = mutableListOf<Coord>()
        val inside = mutableListOf<Coord>()
        // First we special handle the coords that are very obviously outside.
        map.allCoords.filter { map.onEdge(it) }.forEach {
            if (it !in outside && map[it] == ' ') {
                val filled = map.floodFill(it)
                outside.addAll(filled)
                candidateCoords.removeAll(filled)
            }
        }
        for (it in candidateCoords) {
            if (it in inside || it in outside) continue  // Already known
            // count how many times we cross the pipe when moving in astraight line from here out of the map
            val crossings: Int = countCrossings(it)
            val filled = map.floodFill(it)
            if (crossings.odd) {
                inside.addAll(filled)
            } else {
                outside.addAll(filled)
            }
        }
        return inside.size.toLong()
    }

    /**
     *  From the given coord, move vertically out of the map and count how many times we cross the pipe.
     *  We pretend that we are walk as far to the right as possible in the tile so we count only rightwards connections
     */
    private fun countCrossings(coord: Coord): Int {
        require(map[coord] == ' ')
        var c = coord
        var count = 0
        val dir = if (2 * c.y > map.height) Direction.DOWN else Direction.UP
        return map[coord, dir].count { it in "-FL" }
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.testData = true
    day10Problem.runBoth(100)
}
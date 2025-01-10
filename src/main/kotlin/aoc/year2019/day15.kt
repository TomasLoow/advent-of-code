package aoc.year2019

import DailyProblem
import aoc.utils.algorithms.BFS
import aoc.utils.algorithms.BFSNoPathFound
import aoc.utils.algorithms.Dijkstra
import aoc.utils.algorithms.DijkstraResult
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.geometry.steps
import aoc.utils.parseIntCodeComputer
import kotlin.time.ExperimentalTime

enum class Tile {
    UNKNOWN,
    WALL,
    FLOOR,
    OXYGEN
}

class FindUnknown(val map: Array2D<Tile>) : BFS<Coord>({ map[it] == Tile.UNKNOWN }) {
    override fun reachable(state: Coord): Collection<Coord> {
        return state.neighbours(diagonal = false).filter { map[it] != Tile.WALL }
    }
}

class OxygenFlow(val map: Array2D<Tile>) : Dijkstra<Coord>() {
    override fun reachable(state: Coord): Collection<Coord> =
        map.neighbourCoords(state, diagonal = false).filter { map[it] in listOf(Tile.FLOOR, Tile.OXYGEN) }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return 1
    }
}

class Day15Problem : DailyProblem<Int>() {

    override val number = 15
    override val year = 2019
    override val name = "Oxygen System"

    private lateinit var computer: IntCode
    private lateinit var startPos: Coord
    private lateinit var map: Array2D<Tile>
    private lateinit var goal: Coord
    private lateinit var distancesToGoal: DijkstraResult<Coord>

    override fun commonParts() {
        computer = parseIntCodeComputer(getInputText())
        startPos = Coord(51, 51)

        map = exploreMap()
        goal = map.findIndexedByCoordinate { c, v -> v == Tile.OXYGEN }!!.first
        val oxygenFlow = OxygenFlow(map)
        distancesToGoal = oxygenFlow.solveScoreForAllStates(goal)
    }

    /** Explore the whole map by performing BFS searches for one of the closest unknown tiles and moving the robot to it
     */
    fun exploreMap(): Array2D<Tile> {
        var map: Array2D<Tile> = Array2D(101, 101) { c -> Tile.UNKNOWN }
        var robotPos = startPos
        map[robotPos] = Tile.FLOOR
        var bfs = FindUnknown(map)
        while (true) {
            try {
                var path = bfs.solve(robotPos)
                path = path.take(path.indexOfFirst { map[it] == Tile.UNKNOWN } + 1)
                val unknownTile = path.last()
                val (newPos, scan) = followPath(robotPos, path)
                map[unknownTile] = scan
                robotPos = newPos
            } catch (_: BFSNoPathFound) {
                return map
            }
        }
    }

    @Suppress("unused")
    private fun printMap(map: Array2D<Tile>, path: Collection<Coord>) {
        map.mapIndexed { c, v -> if (c in path) "@" else if (v == Tile.WALL) "█" else if (v == Tile.FLOOR) " " else if (v == Tile.OXYGEN) "§" else "~" }
            .print { it }
    }

    private fun followPath(robotPos: Coord, coords: List<Coord>): Pair<Coord, Tile> {
        var res: RunResult? = null
        var pos = robotPos
        coords.steps().forEach { d ->
            computer.writeInput(dirToInp(d))
            res = computer.runUntilNeedsInputOrHalt()
            if (res.output.first() != 0L) pos += d
        }
        val floor = when (res!!.output.first()) {
            2L -> Tile.OXYGEN
            1L -> Tile.FLOOR
            else -> Tile.WALL
        }
        return Pair(pos, floor)
    }

    override fun part1(): Int {
        return distancesToGoal.costs[startPos]!!
    }

    private fun dirToInp(direction: Direction): Long {
        return when (direction) {
            Direction.UP -> 1
            Direction.DOWN -> 2
            Direction.RIGHT -> 3
            Direction.LEFT -> 4
            else -> 999
        }
    }


    override fun part2(): Int {
        return distancesToGoal.costs.maxOf { it.value }
    }
}

val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.testData = false
    day15Problem.runBoth(10)
}
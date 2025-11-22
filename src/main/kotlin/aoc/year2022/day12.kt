package aoc.year2022

import aoc.DailyProblem
import aoc.utils.algorithms.AStar
import aoc.utils.geometry.Array2D
import aoc.utils.algorithms.BFS
import aoc.utils.geometry.Coord
import kotlin.time.ExperimentalTime

class HillClimbBFS(private val map: Array2D<Int>, goal: Coord) : BFS<Coord>(goal) {
    override fun reachable(state: Coord): Collection<Coord> {
        val currentHeight = map[state]
        return map.neighbourCoordsAndValues(state, diagonal = false)
            .filter { (_, h) ->
                (h - 1) <= currentHeight
            }.keys
    }
}

class HillClimbAStar(private val map: Array2D<Int>, goal: Coord) : AStar<Coord>(goal) {
    override fun heuristic(state: Coord): Int {
        return state.manhattanDistanceTo(goal)
    }

    override fun reachable(state: Coord): Collection<Coord> {
        val currentHeight = map[state]
        return map.neighbourCoordsAndValues(state, diagonal = false)
            .filter { (_, h) ->
                (h - 1) <= currentHeight
            }.keys
    }

    override fun getMoveCost(from: Coord, to: Coord): Int = 1
}


class Day12Problem : DailyProblem<Int>() {

    override val number = 12
    override val year = 2022
    override val name = "Hill Climbing Algorithm"
    private lateinit var map: Array2D<Int>

    private var startPos = Coord.origin
    private var endPos = Coord.origin
    private lateinit var hillClimbProblemBFS: HillClimbBFS
    private lateinit var hillClimbProblemAStar: HillClimbAStar

    override fun commonParts() {
        map = Array2D.parseFromLines(getInputText()) { c ->
            when (c) {
                'S' -> -1 //placeholder, replaced below
                'E' -> 999//placeholder, replaced below
                else -> c.code - 'a'.code + 1
            }
        }
        startPos = map.coordOfFirst(-1 )!!
        endPos = map.coordOfFirst(999 )!!
        map[startPos] = 1
        map[endPos] = 26

        hillClimbProblemBFS = HillClimbBFS(map, endPos)
        hillClimbProblemAStar = HillClimbAStar(map, endPos)
    }


    override fun part1(): Int {
        val res = hillClimbProblemBFS.solve(startPos)
        return res.size - 1
    }


    override fun part2(): Int {
        val allLowPoints = map
            .filterToList { coordinate, height ->  // Find coords that are 'a' with a neighbour that is 'b'
                (height == 1) && map
                    .neighbourCoordsAndValues(coordinate)
                    .any { it.value == 2 }
            }.map { it.first }
        return hillClimbProblemAStar.solve(allLowPoints).first

    }
}

val day12Problem = Day12Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day12Problem.runBoth(100)
}
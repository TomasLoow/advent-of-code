package aoc.year2022

import DailyProblem
import aoc.utils.AStar
import aoc.utils.Array2D
import aoc.utils.Coord
import kotlin.time.ExperimentalTime

class HillClimb(protected val map: Array2D<Int>, goal: Coord) : AStar<Coord>(goal) {
    override fun heuristic(state: Coord): Int {
        return state.manhattanDistanceTo(goal)
    }

    override fun reachable(state: Coord): Collection<Coord> {
        val currentHeight = map[state]
        return map.neighbours(state, diagonal = false)
            .filter { (_, h) ->
                (h - 1) <= currentHeight
            }.keys
    }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return 1
    }
}


class Day12Problem : DailyProblem<Int>() {

    override val number = 12
    override val year = 2022
    override val name = "Hill Climbing Algorithm"

    private lateinit var map: Array2D<Int>
    private var startPos = Coord.origin
    private var endPos = Coord.origin
    private lateinit var hillClimbProblem: HillClimb

    override fun commonParts() {
        map = Array2D.parseFromLines(getInputText()) { c ->
            when (c) {
                'S' -> -1 //placeholder, replaced below
                'E' -> 999//placeholder, replaced below
                else -> c.code - 'a'.code + 1
            }
        }
        startPos = map.findIndexedByCoordinate { _, i -> i == -1 }!!.first
        endPos = map.findIndexedByCoordinate { _, i -> i == 999 }!!.first
        map[startPos] = 1
        map[endPos] = 26

        hillClimbProblem = HillClimb(map, endPos)
    }


    override fun part1(): Int {
        val res = hillClimbProblem.solve(startPos)
        return res.first
    }


    override fun part2(): Int {
        val allLowPoints = map
            .filterIndexedByCoordinate { coordinate, height ->  // Find coords that are 'a' with a neighbour that is 'b'
                (height == 1) && map
                    .neighbours(coordinate)
                    .any { it.value == 2 }
            }.map { it.first }
        return hillClimbProblem.solve(allLowPoints).first

    }
}

val day12Problem = Day12Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day12Problem.runBoth(100)
}
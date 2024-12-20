package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime


private class RaceDjik(
    private val map: Array2D<Boolean>,
    goal: Coord,
) : Djikstra<Coord>(goal) {

    override fun reachable(state: Coord): Collection<Coord> {
        return state.neighbours().filter { it in map && !map[it] }.map { it }
    }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return 1
    }
}

class Day20Problem : DailyProblem<Int>() {

    override val number = 20
    override val year = 2024
    override val name = "Race Condition"

    private lateinit var initialMap: Array2D<Boolean>
    private lateinit var start: Coord
    private lateinit var goal: Coord
    private lateinit var fwdCosts: Map<Coord, Int>
    private lateinit var bwdCosts: Map<Coord, Int>
    private var bestCost = 0

    override fun commonParts() {
        val charMap = parseCharArray(getInputText())
        start = charMap.findIndexedByCoordinate { _, c -> c == 'S' }!!.first
        goal = charMap.findIndexedByCoordinate { _, c -> c == 'E' }!!.first
        initialMap = charMap.map { c -> c == '#' }

        // Calculate best path to each point from both directions
        val fwdSolver = RaceDjik(initialMap, goal)
        val bwdSolver = RaceDjik(initialMap, goal)
        bwdCosts = bwdSolver.solve(goal)
        fwdCosts = fwdSolver.solve(start)

        bestCost = fwdCosts[goal]!!
        assert(bestCost == bwdCosts[start]) // Sanity check
    }


    override fun part1(): Int {
        val limit = if (testData) 1 else 100

        val possibleGlitches = initialMap.mapAndFilterToListByNotNull { c, v ->
            if (!v) null
            else {
                c.neighbours().filter { it in initialMap && !initialMap[it] }
            }
        }.filter { it.size >= 2 }

        val q = possibleGlitches.sumOf { glitches ->
            glitches.allUnorderedPairs().count { (n1, n2) ->
                ((fwdCosts[n1]!! + bwdCosts[n2]!! + 2) <= bestCost - limit) || ((bwdCosts[n1]!! + fwdCosts[n2]!! + 2) <= bestCost - limit)
            }
        }

        return q
    }


    override fun part2(): Int {
        val limit = if (testData) 50 else 100
        val openFloors = fwdCosts.keys
        val countCheats = openFloors
            .flatMap { floor ->
                openFloors
                    .filter { other -> other.manhattanDistanceTo(floor) <= 20 }
                    .map { other -> fwdCosts[floor]!! + bwdCosts[other]!! + other.manhattanDistanceTo(floor) }
                    .filter { it <= bestCost - limit }
            }
        return countCheats.size
    }
}


val day20Problem = Day20Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day20Problem.testData = false
    day20Problem.runBoth(20)
}
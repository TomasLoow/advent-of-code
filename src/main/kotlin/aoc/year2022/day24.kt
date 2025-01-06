package aoc.year2022

import DailyProblem
import aoc.utils.algorithms.BFS
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.geometry.Rect
import aoc.utils.parseCharArray
import aoc.utils.parseDirectionFromArrow
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

private data class Storm(var pos: Coord, val direction: Direction, val rect: Rect) {
    fun move() {
        var nextPos = pos + direction
        if (nextPos !in rect) {
            nextPos = when (direction) {
                Direction.UP -> Coord(pos.x, rect.bottomRight.y)
                Direction.DOWN -> Coord(pos.x, rect.topLeft.y)
                Direction.LEFT -> Coord(rect.bottomRight.x, pos.y)
                Direction.RIGHT -> Coord(rect.topLeft.x, pos.y)
                else -> throw Exception("Weird storm tonight...")
            }
        }
        pos = nextPos
    }
}

private data class GaleState(val pos: Coord, val time: Int)

private class GaleBFS(val map: Array2D<Boolean>, val storms: Array<Array2D<Boolean>>, goal: Coord) :
    BFS<GaleState>({ it.pos == goal }) {
    override fun reachable(state: GaleState): Collection<GaleState> {
        val nextTime = state.time + 1
        val stormPositions = storms[nextTime % storms.size]
        return buildList {
            if (!stormPositions[state.pos]) add(GaleState(state.pos, nextTime))
            map.neighbourCoords(state.pos, false).filter { !map[it] && !stormPositions[it] }.forEach {
                add(GaleState(it, nextTime))
            }
        }
    }
}

class Day24Problem : DailyProblem<Int>() {
    override val number = 24
    override val year = 2022

    override val name = "Blizzard Basin"
    private lateinit var initialMap: Array2D<Boolean>
    private lateinit var initialStorms: List<Storm>
    private lateinit var precalcStorms: Array<Array2D<Boolean>>
    private lateinit var start: Coord
    private lateinit var goal: Coord
    private var stormsLoopAfter by Delegates.notNull<Int>()


    override fun commonParts() {
        val a = parseCharArray(getInputText())
        a.xRange.first { a[it, 0] == '.' }.let { start = Coord(it, 0) }
        a.xRange.first { a[it, a.yRange.last] == '.' }.let { goal = Coord(it, a.yRange.last) }
        val stormRect = Rect(topLeft = Coord(1, 1), bottomRight = Coord(a.xRange.last - 1, a.yRange.last - 1))
        initialMap = a.map { it == '#' }
        initialStorms = a.mapAndFilterToListByNotNull { coord, c ->
            if (c in "<>^v") {
                Storm(coord, parseDirectionFromArrow(c), stormRect)
            } else null
        }
        stormsLoopAfter = stormRect.width * stormRect.height
        precalcStorms = Array(stormsLoopAfter) { idx ->
            Array2D(initialMap.width, initialMap.height, false)
        }
        initialStorms.forEach { storm ->
            var st = storm
            repeat(stormsLoopAfter) { idx ->
                precalcStorms[idx][st.pos] = true
                st.move()
            }
        }
    }


    override fun part1(): Int {
        val startState = GaleState(start, 0)
        val solver = GaleBFS(initialMap, precalcStorms, goal)
        val res = solver.solve(startState)
        return res.last().time
    }

    override fun part2(): Int {
        val forwardSolver = GaleBFS(initialMap, precalcStorms, goal)
        val backwardSolver = GaleBFS(initialMap, precalcStorms, start)

        val startState = GaleState(start, 0)
        val firstTrip = forwardSolver.solve(startState)
        val arrivedAtGoalFrustratedAndHeadingHomeState = firstTrip.last()
        val backTrip = backwardSolver.solve(arrivedAtGoalFrustratedAndHeadingHomeState)
        val homeAndFetchingSnacksABitPissedAtThatElfState = backTrip.last()
        val fookingFinallyState = forwardSolver.solve(homeAndFetchingSnacksABitPissedAtThatElfState)
        return fookingFinallyState.last().time
    }
}

val day24Problem = Day24Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day24Problem.testData = false
    day24Problem.runBoth(10)
}
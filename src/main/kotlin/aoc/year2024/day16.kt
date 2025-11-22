package aoc.year2024

import aoc.DailyProblem
import aoc.utils.*
import aoc.utils.algorithms.Dijkstra
import aoc.utils.algorithms.DijkstraResult
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import kotlin.math.min
import kotlin.time.ExperimentalTime

/* Bad, slow and I'm not sure it's guaranteed to give the right answer.
TODO rewrite this days problem */


private data class MazeState(val pos: Coord, val dir: Direction)
private class WalkStar(
    private val map: Array2D<Boolean>,
) : Dijkstra<MazeState>() {

    override fun reachable(state: MazeState): Collection<MazeState> {
        val reachable = buildList {
            if (!map[state.pos + state.dir]) add(state.copy(pos = state.pos + state.dir))
            add(state.copy(dir = state.dir.rotateCW()))
            add(state.copy(dir = state.dir.rotateCCW()))
        }
        return reachable
    }

    override fun getMoveCost(from: MazeState, to: MazeState): Int {
        if (from.pos == to.pos) {
            return 1000
        }
        return 1
    }
}

class Day16Problem : DailyProblem<Int>() {

    override val number = 16
    override val year = 2024
    override val name = "Reindeer Maze"

    private lateinit var initialMap: Array2D<Boolean>
    private lateinit var start: Coord
    private lateinit var goal: Coord
    private lateinit var dijkstraScoresForward: DijkstraResult<MazeState>
    private lateinit var dijkstraScoresBackward: DijkstraResult<MazeState>

    override fun commonParts() {
        val charMap = parseCharArray(getInputText())
        start = charMap.coordOfFirst('S')!!
        goal = charMap.coordOfFirst('E')!!
        initialMap = charMap.map { c -> c == '#' }
        val solver = WalkStar(initialMap)
        dijkstraScoresForward = solver.solveScoreForAllStates(MazeState(start, Direction.RIGHT))
        dijkstraScoresBackward =
            solver.solveScoreForAllStates(listOf(MazeState(goal, Direction.LEFT), MazeState(goal, Direction.DOWN)))

    }

    override fun part1(): Int {
        // We don't care how we are rotated at the end.
        return min(
            dijkstraScoresForward.costs.getValue(MazeState(goal, Direction.UP)),
            dijkstraScoresForward.costs.getValue(MazeState(goal, Direction.RIGHT))
        )
    }

    override fun part2(): Int {
        val bestScore = min(
            dijkstraScoresForward.costs.getValue(MazeState(goal, Direction.UP)),
            dijkstraScoresForward.costs.getValue(MazeState(goal, Direction.RIGHT))
        )
        val allStates = dijkstraScoresForward.costs.keys
        // Find all states where the cost from the start here plus the cost from here to the end == bestScore
        return allStates
            .filter {
                dijkstraScoresForward.costs[it]!! + dijkstraScoresBackward.costs[it.copy(dir = it.dir.rotate180())]!! == bestScore
            }
            .map { it.pos }.distinct()  // We want to count coords, not states (coord + dir)
            .count()
    }
}

val day16Problem = Day16Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day16Problem.testData = false
    day16Problem.runBoth(100)
}
package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

/* Bad, slow and I'm not sure it's guaranteed to give the right answer.
TODO rewrite this days problem */


private data class MazeState(val pos: Coord, val dir: Direction)
private class WalkStar(
    private val map: Array2D<Boolean>,
    goal: MazeState,
    val blocked: Coord? = null
) : AStar<MazeState>(goal) {

    override fun heuristic(state: MazeState): Int {
        val cost = state.pos.manhattanDistanceTo(goal.pos)
        if (state.dir in listOf(Direction.DOWN, Direction.LEFT)) {
            return cost + 2000
        }
        if (state.pos.x != goal.pos.x && state.pos.y != goal.pos.y) {
            return cost + 1000
        }
        return cost
    }

    override fun isGoal(state: MazeState): Boolean { return state.pos == goal.pos}

    override fun reachable(state: MazeState): Collection<MazeState> {
        val reachable = buildList {
            if (!map[state.pos + state.dir]) add(state.copy(pos = state.pos + state.dir))
            add(state.copy(dir = state.dir.rotateCW()))
            add(state.copy(dir = state.dir.rotateCCW()))
        }

        if (blocked != null) return reachable.filter { it.pos != blocked }
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

    override fun commonParts() {
        val charMap = parseCharArray(getInputText())
        start = charMap.findIndexedByCoordinate { _, c -> c == 'S' }!!.first
        goal = charMap.findIndexedByCoordinate { _, c -> c == 'E' }!!.first
        initialMap = charMap.map { c -> c == '#' }
    }

    private fun solve(): Pair<Int, List<MazeState>> {
        val solver = WalkStar(initialMap, MazeState(goal, Direction.UPRIGHT))
        return solver.solve(MazeState(start, Direction.RIGHT))
    }

    override fun part1(): Int {
        return solve().first
    }

    override fun part2(): Int {
        val shouldTryToBlock = mutableSetOf<Coord>()
        val triedToBlock = mutableSetOf<Coord>()
        val seen = mutableSetOf<Coord>()
        val basicSolution = solve()
        var bestScore = basicSolution.first
        shouldTryToBlock.addAll(basicSolution.second.map { it.pos })
        seen.addAll(basicSolution.second.map { it.pos })

        while (shouldTryToBlock.isNotEmpty()) {

            val b = shouldTryToBlock.first()
            shouldTryToBlock.remove(b)
            triedToBlock.add(b)
            val solver = WalkStar(initialMap, blocked = b, goal = MazeState(goal, Direction.UP))

            try {
                val s = solver.solve(MazeState(start, Direction.RIGHT))
                if (s.first > bestScore) continue
                else bestScore = s.first
                //shouldTryToBlock.addAll(s.second.map { it.pos })
                seen.addAll(s.second.map { it.pos })
            } catch (e: Exception) {
                // pass
            }
            shouldTryToBlock.removeAll(triedToBlock)
            // println("shouldTryToBlock: ${shouldTryToBlock.size}")
            // println("triedToBlock: ${triedToBlock.size}")
            // println("seen: ${seen.size}")
        }

        return seen.size
    }
}

val day16Problem = Day16Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day16Problem.testData = true
    day16Problem.runBoth(1)
}
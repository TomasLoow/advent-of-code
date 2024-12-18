package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime


class RAMRun(val blocked: List<Coord>, val rect: Rect, goal: Coord) : AStar<Coord>(goal) {
    override fun heuristic(state: Coord): Int {
        return state.manhattanDistanceTo(goal)
    }

    override fun isGoal(state: Coord): Boolean {
        return state == goal
    }

    override fun reachable(state: Coord): Collection<Coord> {
        return state.neighbours().filter { it !in blocked }.filter { it in rect }
    }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return 1
    }
}

class Day18Problem : DailyProblem<String>() {

    override val number = 18
    override val year = 2024
    override val name = "RAM run"

    private lateinit var data: List<Coord>
    private var size = 0
    private var part1Steps = 0

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map(::parseCoord)
        size = if (testData) 6 else 70
        part1Steps = if (testData) 12 else 1024
    }

    fun solveForSteps(steps: Int): Pair<Int, List<Coord>> {
        val solver = RAMRun(data.take(steps), Rect(Coord(0, 0), Coord(size, size)), Coord(size, size))
        val res = solver.solve(Coord(0, 0))
        return res
    }


    override fun part1(): String {
        return solveForSteps(part1Steps).first.toString()
    }


    override fun part2(): String {
        // Bisect until we find the border between solvable and unsolvable
        var good = part1Steps
        var bad = data.size
        while (good + 1 < bad) {
            val mid = (good + bad) / 2
            try {
                solveForSteps(mid)
                good = mid

            } catch (e: Exception) {
                bad = mid
            }
        }
        val c = (data[bad - 1])  // if we fail at step n then we have used blocks 0..(n-1).
        return "${c.x},${c.y}"
    }
}

val day18Problem = Day18Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day18Problem.testData = false
    day18Problem.runBoth(1)
}
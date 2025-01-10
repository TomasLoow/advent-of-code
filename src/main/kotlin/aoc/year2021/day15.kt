package aoc.year2021

import DailyProblem
import aoc.utils.algorithms.AStar
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.parseIntArray
import kotlin.time.ExperimentalTime

private fun embiggen(originalMap: Array2D<Int>): Array2D<Int> {
    val factor = 5
    val newHeight = originalMap.height * factor
    val newWidth = originalMap.width * factor
    val biggerArray: Array2D<Int> = Array2D(newWidth, newHeight) { c ->
        val orgX = c.x.mod(originalMap.width)
        val orgY = c.y.mod(originalMap.height)
        val increase = c.x / originalMap.width + c.y / originalMap.height
        val baseValue = originalMap[orgX, orgY] + increase
        if (baseValue > 9) {
            baseValue - 9
        } else {
            baseValue
        }
    }
    return biggerArray
}


private class ChitinAStar(val map: Array2D<Int>, goal: Coord) : AStar<Coord>(goal) {
    override fun heuristic(state: Coord): Int {
        return 0  // 0 means it's actually dijkstra's algorithm. The manhattan heuristics did not really help
        // return (map.size - pos.second) + (map[0].size - pos.first)
    }

    override fun reachable(state: Coord): Collection<Coord> {
        return map.neighbourCoords(state, diagonal = false)
    }

    override fun getMoveCost(from: Coord, to: Coord): Int {
        return map[to]
    }

}

class Day15Problem : DailyProblem<Long>() {
    override val number = 15
    override val year = 2021
    override val name = "Chiton"

    private lateinit var risks: Array2D<Int>

    override fun commonParts() {
        this.risks = parseIntArray(getInputText())
    }

    override fun part1(): Long {
        val prob = ChitinAStar(risks, risks.rect.bottomRight)
        return prob.solve(Coord.origin).first.toLong()
    }

    override fun part2(): Long {
        val moreRisks = embiggen(risks)
        val prob = ChitinAStar(moreRisks, moreRisks.rect.bottomRight)
        return prob.solve(Coord.origin).first.toLong()
    }
}

val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.runBoth(5)
}
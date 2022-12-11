package aoc.year2021

import DailyProblem
import aoc.utils.Array2D
import aoc.utils.Coord

import aoc.utils.parseIntArray
import java.io.File
import java.util.*
import kotlin.time.ExperimentalTime

fun parseArray2D(path: String): Array2D<Int> {
    return parseIntArray(File(path).readText())
}

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

fun heuristic(map: Array2D<Int>, pos: Coord): Int {
    return 0  // 0 means it's actually djikstra's algorithm. The manhattan heuristics did not really help
    // return (map.size - pos.second) + (map[0].size - pos.first)
}

fun getRisk(map: Array2D<Int>, from: Coord, to: Coord): Int {
    return map[to]
}

fun neighboursOf(map: Array2D<Int>, c: Coord): List<Coord> {
    return map.neighbourCoords(c, diagonal = false)
}


fun <Env, State> aStar(map: Env,
                 start: State,
                 goal: State,
                 heur: (Env, State)-> Int,
                 neigh: (Env, State) -> Collection<State>,
                 getEdgeValue: (Env, State, State) -> Int): Int {
    val openSet = PriorityQueue<Pair<Int,State>>(compareBy { it.first })
    openSet.add(heur(map,start) to start)

    val cameFrom: MutableMap<State, State> = mutableMapOf()
    val cheapestPathScoreMap : MutableMap<State, Int> = mutableMapOf(start to 0)
    val heuristicScoreMap: MutableMap<State, Int> = mutableMapOf(start to heur(map, start))

    while (openSet.isNotEmpty()) {
        val currentPair = openSet.first()!!
        val current= currentPair.second
        if (current == goal) {
            return cheapestPathScoreMap[current]!!
        }
        openSet.remove(currentPair)

        for (neighbour: State in neigh(map, current)) {
            var neighbourValue: Int
            try {
                neighbourValue = getEdgeValue(map, current, neighbour)
            } catch (e: ArrayIndexOutOfBoundsException) {
                continue
            }
            val tentative_gScore = cheapestPathScoreMap.getOrDefault(current, Int.MAX_VALUE) + neighbourValue
            if (tentative_gScore < cheapestPathScoreMap.getOrDefault(neighbour, Int.MAX_VALUE)) {
                cameFrom[neighbour] = current
                cheapestPathScoreMap[neighbour] = tentative_gScore
                heuristicScoreMap[neighbour] = tentative_gScore + heur(map, neighbour)
                openSet.add(heuristicScoreMap[neighbour]!! to neighbour)
            }
        }
    }
    throw Exception("No path")
}

class Day15Problem() : DailyProblem<Long>() {
    override val number = 15
    override val year = 2021
    override val name = "Chiton"

    private lateinit var risks: Array2D<Int>

    override fun commonParts() {
        this.risks = parseArray2D(getInputFile().absolutePath)
    }

    override fun part1(): Long {
        return aStar<Array2D<Int>, Coord>(
            risks,
            Coord.origin,
            risks.rect.bottomRight,
            ::heuristic,
            ::neighboursOf,
            ::getRisk
        ).toLong()
    }

    override fun part2(): Long {
        val moreRisks = embiggen(risks)
        return aStar<Array2D<Int>, Coord>(
            moreRisks,
            Coord.origin,
            moreRisks.rect.bottomRight,
            ::heuristic,
            ::neighboursOf,
            ::getRisk
        ).toLong()
    }
}

val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.runBoth(5)
}
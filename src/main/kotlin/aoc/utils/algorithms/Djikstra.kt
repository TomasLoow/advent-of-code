@file:Suppress("unused")
package aoc.utils.algorithms

import aoc.utils.emptyMutableSet
import java.util.*

data class DijkstraResult<S>(val costs: MutableMap<S, Int>, val cameFrom: Map<S, S>)

abstract class Dijkstra<State>() {
    var steps = 0

    abstract fun reachable(state: State): Collection<State>
    abstract fun getMoveCost(from: State, to: State): Int

    fun solveScoreForAllStates(start: State): DijkstraResult<State> {
        return solveScoreForAllStates(listOf(start))
    }

    fun reconstructPathTo(cameFrom: Map<State, State>, goal: State): List<State> {
        return buildList {
            add(goal)
            var s = goal
            while (s in cameFrom) {
                s = cameFrom[s]!!
                add(s)
            }
        }.reversed()
    }


    fun solveScoreForAllStates(startStates: List<State>): DijkstraResult<State> {
        steps = 0
        val openSet = emptyMutableSet<State>()
        val cameFrom: MutableMap<State, State> = HashMap(4096)
        val cheapestPathScoreMap : MutableMap<State, Int> = HashMap(4096)
        startStates.forEach { start ->
            openSet.add(start)
            cheapestPathScoreMap[start] = 0
        }

        while (openSet.isNotEmpty()) {
            steps++
            val current = openSet.first()!!
            openSet.remove(current)
            for (neighbour: State in reachable(current)) {
                var neighbourValue: Int
                try {
                    neighbourValue = getMoveCost(current, neighbour)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    continue
                }
                val tentativeGScore = cheapestPathScoreMap.getOrDefault(current, Int.MAX_VALUE) + neighbourValue
                if (tentativeGScore < cheapestPathScoreMap.getOrDefault(neighbour, Int.MAX_VALUE)) {
                    cameFrom[neighbour] = current
                    cheapestPathScoreMap[neighbour] = tentativeGScore
                    openSet.add(neighbour)
                }
            }
        }
        return DijkstraResult(costs = cheapestPathScoreMap, cameFrom = cameFrom)
    }
}
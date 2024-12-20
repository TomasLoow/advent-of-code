package aoc.utils

import java.util.*

abstract class Djikstra<State>(val goal:State) {
    var steps = 0

    abstract fun reachable(state: State): Collection<State>
    abstract fun getMoveCost(from: State, to: State): Int

    fun solve(start: State): MutableMap<State, Int> {
        return solve(listOf(start))
    }

    fun solve(startStates: List<State>): MutableMap<State, Int> {
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
        return cheapestPathScoreMap
    }
}
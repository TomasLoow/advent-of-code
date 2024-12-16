package aoc.utils

import java.util.*

abstract class AStar<State>(val goal:State) {
    var steps = 0

    /** heuristic function.
     * Should be
     * admissible       (https://en.wikipedia.org/wiki/Admissible_heuristic)
     * and consistent   (https://en.wikipedia.org/wiki/Consistent_heuristic)
     */
    abstract fun heuristic(state: State):Int
    abstract fun reachable(state: State): Collection<State>
    abstract fun getMoveCost(from: State, to: State): Int

    fun reconstructPath(cameFrom:MutableMap<State, State>): List<State> {
        return buildList {
            add(goal)
            var s = goal
            while(s in cameFrom) {
                s = cameFrom[s]!!
                add(s)
            }
        }.reversed()
    }

    fun solve(start: State): Pair<Int, List<State>> {
        return solve(listOf(start))
    }

    fun solve(startStates: List<State>): Pair<Int, List<State>> {
        steps = 0
        val openSet = PriorityQueue<Pair<Int, State>>(compareBy { it.first })
        val cameFrom: MutableMap<State, State> = HashMap(4096)
        val cheapestPathScoreMap : MutableMap<State, Int> = HashMap(4096)
        val heuristicScoreMap: MutableMap<State, Int> = HashMap(4096)
        startStates.forEach { start ->
            openSet.add(heuristic(start) to start)
            cheapestPathScoreMap[start] = 0
            heuristicScoreMap[start] = heuristic(start)
        }

        while (openSet.isNotEmpty()) {
            steps++
            val currentPair = openSet.first()!!
            val current= currentPair.second
            if (current == goal) {
                return Pair(cheapestPathScoreMap[current]!!, reconstructPath(cameFrom))
            }
            openSet.remove(currentPair)

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
                    heuristicScoreMap[neighbour] = tentativeGScore + heuristic(neighbour)
                    openSet.add(heuristicScoreMap[neighbour]!! to neighbour)
                }
            }
        }
        throw Exception("No path")
    }
}
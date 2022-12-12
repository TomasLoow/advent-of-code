package aoc.utils

abstract class BFS<State>(val goal: State) {
    abstract fun reachable(state: State): Collection<State>

    fun reconstructPath(cameFrom: MutableMap<State, State>): List<State> {
        return buildList<State> {
            add(goal)
            var s = goal
            while (s in cameFrom) {
                s = cameFrom[s]!!
                add(s)
            }
        }.reversed()
    }

    fun solve(startState: State): List<State> {
        val q = ArrayDeque<State>()
        q.addLast(startState)
        val cameFrom = mutableMapOf<State, State>() //
        val explored = mutableSetOf(startState)
        while (q.isNotEmpty()) {
            val v = q.removeFirst()
            if (v == goal) {
                return reconstructPath(cameFrom)
            }
            reachable(v).forEach { n ->
                if (n !in explored) {
                    explored.add(n)
                    cameFrom[n] = v
                    q.addLast(n)
                }
            }
        }
        throw Exception("No path found")
    }
}
package aoc.utils

abstract class BFS<State> {

    val isGoal: (State) -> Boolean

    constructor(goal: State) {
        this.isGoal = { it == goal }
    }

    constructor(isGoal: (State) -> Boolean) {
        this.isGoal = isGoal
    }

    abstract fun reachable(state: State): Collection<State>

    fun reconstructPath(cameFrom: MutableMap<State, State>, goal: State): List<State> {
        return buildList {
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
        val cameFrom = emptyMutableMap<State, State>() //
        val explored = mutableSetOf(startState)
        while (q.isNotEmpty()) {
            val v = q.removeFirst()
            if (isGoal(v)) {
                return reconstructPath(cameFrom, v)
            }
            reachable(v).forEach { n ->
                if (n !in explored) {
                    explored.add(n)
                    cameFrom[n] = v
                    q.addLast(n)
                }
            }
        }
        @Suppress("UNCHECKED_CAST")
        throw BFSNoPathFound(explored as Set<Any>)
    }
}
class BFSNoPathFound(val explored: Set<Any>) : Throwable()


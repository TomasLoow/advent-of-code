package aoc.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertContains
import kotlin.test.assertTrue


/** Test the AStar algorithm with a simple labyrinth pathfinding */
class AStarTest {

    open class AStarExample(private val map: Array2D<Boolean>, goal: Coord) : AStar<Coord>(goal) {
        override fun heuristic(state: Coord): Int {
            return state.chebyshevDistanceTo(goal)
        }

        override fun reachable(state: Coord): Collection<Coord> {
            return map.neighbourCoords(state, diagonal = true).filter { !map[it] }
        }

        override fun getMoveCost(from: Coord, to: Coord): Int {
            return 1 // All moves cost 1
        }

    }

    class AStarExampleWithoutHeur(map: Array2D<Boolean>, goal: Coord) : AStarExample(map, goal) {
        override fun heuristic(state: Coord): Int {
            return 0
        }
    }

    val labyrinth = """
            ....█...........
            ....█...........
            ....█.....██....
            .████......████.
            ....█.........█.
            ....███████...█.
            ..............█.
            ..............█.
        """.trimIndent()
    /* The solution should be something like:
       S...█...........
       ¤...█.....¤¤....
       ¤...█....¤██¤¤¤.
       ¤████.....¤████¤
       .¤¤.█.....¤...█¤
       ...¤███████¤..█¤
       ....¤¤¤¤¤¤¤...█¤
       ..............█¤
     */

    @Test
    fun testAStar() {

        val map = Array2D.parseFromLines(labyrinth.trimIndent(), { it == '█' })
        val goal = map.rect.bottomRight
        val asExample = AStarExample(map, goal)
        val (cost, path) = asExample.solve(Coord.origin)

        assertEquals(27, cost)
        assertContains(path, Coord(0, 3))
        assertContains(path, Coord(11, 1))


    }

    @Test
    fun `test heuristics improves AStar search`() {
        val map = Array2D.parseFromLines(labyrinth, { it == '█' })
        val goal = map.rect.bottomRight
        val asExample = AStarExample(map, goal)
        val asExample2 = AStarExampleWithoutHeur(map, goal)
        asExample.solve(Coord.origin)
        asExample2.solve(Coord.origin)
        assertTrue { asExample.steps < asExample2.steps }
    }
}
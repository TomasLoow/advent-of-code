@file:Suppress("unused")

package aoc.utils.geometry

import kotlin.math.absoluteValue


enum class Axis2D {
    X, Y
}

enum class Axis3D {
    X, Y, Z
}

typealias Vector = Pair<Int, Int>

operator fun Vector.plus(v2: Vector) = Pair(first + v2.first, second + v2.second)
operator fun Vector.times(factor: Int) = Pair(first * factor, second * factor)
operator fun Int.times(pair: Vector) = Pair(pair.first * this, pair.second * this)

fun Vector.decomposeDirs(): Map<Direction, Int> {
    return buildMap {
        if (first < 0) put(Direction.LEFT, first.absoluteValue)
        if (first > 0) put(Direction.RIGHT, first.absoluteValue)
        if (second < 0) put(Direction.UP, second.absoluteValue)
        if (second > 0) put(Direction.DOWN, second.absoluteValue)
    }
}

/**
 * For a list of coordinates, return the cartesian directions that need to be taken to follow it.
 *
 * Only works if each coord has manhattandistance == 1 to its adjacent coords
 */
fun List<Coord>.steps(): List<Direction> {
    return this.zipWithNext { c, nextC ->
        if (nextC.x > c.x) Direction.RIGHT
        else if (nextC.x < c.x) Direction.LEFT
        else if (nextC.y > c.y) Direction.DOWN
        else Direction.UP
    }
}

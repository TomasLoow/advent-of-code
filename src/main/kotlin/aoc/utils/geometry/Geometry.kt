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



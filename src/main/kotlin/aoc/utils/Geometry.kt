package aoc.utils

import java.lang.Math.abs
import java.lang.Math.max


enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    fun rotateCW(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun rotateCCW(): Direction {
        return when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
        }
    }
}

data class Coord(val x: Int, val y: Int) {

    operator fun plus(d: Direction) = this.stepInDir(d)
    operator fun plus(delta: Pair<Int, Int>) = Coord(x + delta.first, y + delta.second)

    operator fun minus(other: Coord): Pair<Int, Int> = Pair(x - other.x, y - other.y)

    fun manhattanDistanceTo(other: Coord): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    /** Also known as L_inf norm or Max-distance */
    fun chebyshevDistanceTo(other: Coord): Int {
        return max(abs(x - other.x), abs(y - other.y))
    }

    fun stepInDir(d: Direction): Coord {
        val (x, y) = this
        return when (d) {
            Direction.UP -> Coord(x, y - 1)
            Direction.RIGHT -> Coord(x + 1, y)
            Direction.DOWN -> Coord(x, y + 1)
            Direction.LEFT -> Coord(x - 1, y)
        }
    }

    fun walkInDir(d: Direction): Sequence<Coord> {
        var (x, y) = this
        var dx = 0
        var dy = 0
        when (d) {
            Direction.UP -> dy = -1
            Direction.RIGHT -> dx = 1
            Direction.DOWN -> dy = 1
            Direction.LEFT -> dx = -1
        }
        return sequence {
            while (true) {
                yield(Coord(x, y))
                x += dx
                y += dy
            }
        }
    }

    companion object {
        val origin = Coord(0, 0)
    }
}

data class Rect(val topLeft: Coord, val bottomRight: Coord) {
    val width: Int
        get() {
            return bottomRight.x - topLeft.x + 1
        }

    val height: Int
        get() {
            return bottomRight.y - topLeft.y + 1
        }

    val xRange: IntRange
        get() {
            return (topLeft.x..bottomRight.x)
        }

    val yRange: IntRange
        get() {
            return (topLeft.y..bottomRight.y)
        }

    operator fun contains(point: Coord): Boolean {
        return point.x in xRange && point.y in yRange
    }
}

fun main() {
    val c = Coord(2, 3)
    val c2 = Coord(6, 11)
    val r = Rect(c, c2)
    println(r.width)
    println(r.height)
}



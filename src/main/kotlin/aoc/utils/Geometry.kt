package aoc.utils

import java.lang.Math.max
import java.lang.Math.min
import kotlin.math.absoluteValue

/*
In this module (0,0) is always the TOP LEFT corner
with x,y increasing towards down and right. I blame QBasic.
*/

enum class Axis2D {
    X, Y
}

enum class Axis3D {
    X, Y, Z
}

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
    UPRIGHT,
    UPLEFT,
    DOWNRIGHT,
    DOWNLEFT;

    fun rotateCW(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            else -> TODO()
        }
    }

    fun rotateCCW(): Direction {
        return when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
            else -> TODO()
        }
    }

    fun toArrowChar() : Char {
        return when(this) {
            UP -> '^'
            RIGHT -> '>'
            DOWN -> 'v'
            LEFT -> '<'
            else -> throw Exception("not a cartesian direction")
        }
    }


}

data class Coord(val x: Int, val y: Int) {

    override fun hashCode(): Int {
        return (x * MAX_VALUE + y) // This will be collision free for all x & y < MAX_VALUE
    }

    override fun equals(other: Any?): Boolean {
        other as Coord
        return (x == other.x && y == other.y)
    }
    operator fun plus(d: Direction) = this.stepInDir(d)

    operator fun plus(delta: Pair<Int, Int>) = copy(x = x + delta.first, y = y + delta.second)
    operator fun minus(other: Coord): Pair<Int, Int> = Pair(x - other.x, y - other.y)

    operator fun minus(delta: Pair<Int, Int>) = copy(x = x - delta.first, y = y - delta.second)

    fun manhattanDistanceTo(other: Coord): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }


    /** Also known as L_inf norm or Max-distance */
    fun chebyshevDistanceTo(other: Coord): Int {
        return max((x - other.x).absoluteValue, (y - other.y).absoluteValue)
    }

    fun stepInDir(d: Direction): Coord {
        val (x, y) = this
        return when (d) {
            Direction.UP -> copy(y = y - 1)
            Direction.RIGHT -> copy(x = x + 1)
            Direction.DOWN -> copy(y = y + 1)
            Direction.LEFT -> copy(x = x - 1)
            Direction.UPRIGHT -> copy(x = x + 1, y = y - 1)
            Direction.UPLEFT -> copy(x = x - 1, y = y - 1)
            Direction.DOWNRIGHT -> copy(x = x + 1, y = y + 1)
            Direction.DOWNLEFT -> copy(x = x - 1, y = y + 1)

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
            Direction.UPLEFT -> {
                dy = -1
                dx = -1
            }
            Direction.UPRIGHT -> {
                dy = -1
                dx = 1
            }
            Direction.DOWNLEFT -> {
                dy = 1
                dx = -1
            }
            Direction.DOWNRIGHT -> {
                dy = 1
                dx = 1
            }
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
        private val MAX_VALUE = 1024

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

    companion object {
        fun boundingCoords(points: Collection<Coord>): Rect {
            var minX = Int.MAX_VALUE
            var minY = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE
            var maxY = Int.MIN_VALUE

            points.forEach { (x, y) ->
                minX = min(minX, x)
                minY = min(minY, y)
                maxX = max(maxX, x)
                maxY = max(maxY, y)
            }
            return Rect(Coord(minX, minY), Coord(maxX, maxY))
        }
    }
}

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

    fun rotate180(): Direction {
        return when (this) {
            UP -> DOWN
            RIGHT -> LEFT
            DOWN -> UP
            LEFT -> RIGHT
            else -> TODO()
        }
    }

    fun toArrowChar(): Char {
        return when (this) {
            UP -> '^'
            RIGHT -> '>'
            DOWN -> 'v'
            LEFT -> '<'
            UPRIGHT -> '7'
            UPLEFT -> 'F'
            DOWNRIGHT -> 'J'
            DOWNLEFT -> 'L'
        }
    }

    companion object {
        val cartesian get() = listOf(UP, RIGHT, DOWN, LEFT)
        val diagonal get() = listOf(UPRIGHT, DOWNRIGHT, DOWNLEFT, UPLEFT)
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

    operator fun plus(delta: Vector) = copy(x = x + delta.first, y = y + delta.second)
    operator fun minus(other: Coord): Vector = Pair(x - other.x, y - other.y)

    operator fun minus(delta: Vector) = copy(x = x - delta.first, y = y - delta.second)

    fun manhattanDistanceTo(other: Coord): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }


    /** Also known as L_inf norm or Max-distance */
    fun chebyshevDistanceTo(other: Coord): Int {
        return max((x - other.x).absoluteValue, (y - other.y).absoluteValue)
    }

    fun neighbours(diagonal: Boolean = false): List<Coord> = if (!diagonal) listOf(
        this.copy(x = this.x - 1),
        this.copy(x = this.x + 1),
        this.copy(y = this.y - 1),
        this.copy(y = this.y + 1)
    ) else listOf(
        this.copy(x = this.x - 1),
        this.copy(x = this.x + 1),
        this.copy(y = this.y - 1),
        this.copy(y = this.y + 1),
        this.copy(x = this.x - 1, y = this.y - 1),
        this.copy(x = this.x + 1, y = this.y + 1),
        this.copy(x = this.x - 1, y = this.y + 1),
        this.copy(x = this.x + 1, y = this.y - 1),
    )

    fun isNeighbourWith(other: Coord, diagonal: Boolean = false): Boolean {
        return if (!diagonal) this.manhattanDistanceTo(other) == 1
        else this.chebyshevDistanceTo(other) == 1
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

    fun toPair(): Pair<Int, Int> = Pair(x, y)

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

    val topRight: Coord
        get() {
            return Coord(bottomRight.x, topLeft.y)
        }

    val bottomLeft: Coord
        get() {
            return Coord(topLeft.x, bottomRight.y)
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
        fun bounding(points: Collection<Coord>): Rect {
            data class Acc(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int)
            val (minX, minY, maxX, maxY) = points.fold(
                Acc(minX = Int.MAX_VALUE, minY = Int.MAX_VALUE, maxX = Int.MIN_VALUE, maxY = Int.MIN_VALUE)
            ) { acc: Acc, coord: Coord ->
                Acc(
                    minX = min(coord.x, acc.minX),
                    minY = min(coord.y, acc.minY),
                    maxX = max(coord.x, acc.maxX),
                    maxY = max(coord.y, acc.maxY)
                )
            }
            return Rect(Coord(minX, minY), Coord(maxX, maxY))
        }
    }
}

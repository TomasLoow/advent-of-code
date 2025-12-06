@file:Suppress("unused")
package aoc.utils.geometry

import kotlin.math.absoluteValue
import kotlin.math.max

data class Coord(val x: Int, val y: Int) {

    override fun hashCode(): Int {
        return (x * MAX_VALUE + y) // This will be collision-free for all x & y < MAX_VALUE
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

    fun stepInDir(d: Direction, length:Int = 1): Coord {
        val (x, y) = this
        return when (d) {
            Direction.UP -> copy(y = y - length)
            Direction.RIGHT -> copy(x = x + length)
            Direction.DOWN -> copy(y = y + length)
            Direction.LEFT -> copy(x = x - length)
            Direction.UPRIGHT -> copy(x = x + length, y = y - length)
            Direction.UPLEFT -> copy(x = x - length, y = y - length)
            Direction.DOWNRIGHT -> copy(x = x + length, y = y + length)
            Direction.DOWNLEFT -> copy(x = x - length, y = y + length)

        }
    }

    /**
     * infinite sequence *starting with this coord* and walking in the given direction
     */
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
    fun toVector(): Vector = Vector(x, y)

    companion object {
        val origin = Coord(0, 0)
        private const val MAX_VALUE = 1024

    }
}
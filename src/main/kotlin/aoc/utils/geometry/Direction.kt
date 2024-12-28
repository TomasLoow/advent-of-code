@file:Suppress("unused")
package aoc.utils.geometry

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
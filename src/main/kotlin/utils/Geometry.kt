package utils

typealias Coord = Pair<Int, Int>

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

fun Coord.stepInDir(d: Direction): Coord {
    val (x,y) = this
    return when(d) {
        Direction.UP -> Coord(x,y-1)
        Direction.RIGHT -> Coord(x+1,y)
        Direction.DOWN -> Coord(x,y+1)
        Direction.LEFT -> Coord(x-1,y)
    }
}

fun Coord.walkInDir(d: Direction): Sequence<Coord> {
    var (x,y) = this
    var dx = 0
    var dy = 0
    when(d) {
        Direction.UP -> dy = -1
        Direction.RIGHT -> dx = 1
        Direction.DOWN -> dy = 1
        Direction.LEFT -> dx = -1
    }
    return sequence {
        while(true) {
            yield(Coord(x,y))
            x +=dx
            y+=dy
        }
    }
}
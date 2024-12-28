@file:Suppress("unused")
package aoc.utils.geometry

import kotlin.math.max
import kotlin.math.min

data class Rect(val topLeft: Coord, val bottomRight: Coord) {
    val width: Int
        get() {
            return bottomRight.x - topLeft.x + 1
        }

    val height: Int
        get() {
            return bottomRight.y - topLeft.y + 1
        }

    val area: Int
        get() {
            return width * height
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
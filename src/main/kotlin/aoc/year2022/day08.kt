package aoc.year2022

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.parseIntArray
import kotlin.time.ExperimentalTime

class Day08Problem : DailyProblem<Int>() {

    override val number = 8
    override val year = 2022
    override val name = "Treetop Tree House"


    private lateinit var forrestMap: Array2D<Int>

    private fun parseFile(): Array2D<Int> {
        return parseIntArray(getInputText())
    }

    private fun canBeSeenFromOutside(map: Array2D<Int>, pos: Coord, h: Int): Boolean {
        val (x,y) = pos
        if (map.onEdge(pos)) return true

        val visibleFromLeft = (0 until x).all { i -> map[i,y] < h }
        val visibleFromRight = (x + 1 until map.width).all { i -> map[i,y] < h }

        val visibleFromTop = (0 until y).all { i -> map[x,i] < h }
        val visibleFromBottom = (y + 1 until map.height).all { i -> map[x,i] < h }

        return visibleFromLeft || visibleFromRight || visibleFromTop || visibleFromBottom
    }

    private fun scenicScore(map: Array2D<Int>, pos: Coord, height: Int): Int {

        val (x,y) = pos
        if (map.onEdge(pos)) return 0

        var lookingUp = (y - 1 downTo 0).takeWhile { yi -> map[x,yi] < height }.count()
        var lookingLeft = (x - 1 downTo 0).takeWhile { xi -> map[xi,y] < height }.count()
        var lookingDown = (y + 1 until map.height).takeWhile { yi -> map[x,yi] < height }.count()
        var lookingRight = (x + 1 until map.width).takeWhile { xi -> map[xi,y] < height }.count()

        /* If we hit the end of the map the values are correct, otherwise add 1 to each */
        if (lookingUp != y) lookingUp++
        if (lookingLeft != x) lookingLeft++
        if (lookingDown != map.height-y-1) lookingDown++
        if (lookingRight != map.width-x-1) lookingRight++

        return lookingLeft * lookingRight * lookingUp * lookingDown
    }

    override fun commonParts() {
        forrestMap = parseFile()
    }

    override fun part1(): Int {
        return forrestMap.countIndexedByCoordinate { c, h ->
            canBeSeenFromOutside(forrestMap, c, h)
        }
    }

    override fun part2(): Int {
        return forrestMap.mapAndFilterToListByNotNull { c, height ->
            scenicScore(forrestMap, c, height)
        }.maxOf { it }
    }
}


val day08Problem = Day08Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day08Problem.runBoth(1000)
}
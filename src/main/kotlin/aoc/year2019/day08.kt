package aoc.year2019

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.showBooleanArray
import aoc.utils.parseDisplay
import kotlin.time.ExperimentalTime

class Day08Problem : DailyProblem<String>() {

    override val number = 8
    override val year = 2019
    override val name = "Space Image Format"

    private lateinit var layers: List<Array2D<Int>>

    override fun commonParts() {
        layers = getInputText().lines().first().map { c -> c.toString().toInt()}.chunked(25*6).map {
            Array2D(it, 25,6)
        }
    }


    override fun part1(): String {
        val l = layers.minBy { a ->
            a.countIndexedByCoordinate { _, v -> v == 0 }
        }
        return (l.countIndexedByCoordinate { _, v -> v == 1 } * l.countIndexedByCoordinate { _, v -> v == 2 }).toString()
    }


    override fun part2(): String {
        val res = Array2D(25, 6) { (x,y) ->
            layers.firstOrNull { it[x,y] != 2 }?.get(x,y) ?: 0
        }
        val s = showBooleanArray(res.map { it == 1 })
        parseDisplay(s)
        return parseDisplay(s)
    }
}

val day08Problem = Day08Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day08Problem.testData = false
    day08Problem.runBoth(100)
}
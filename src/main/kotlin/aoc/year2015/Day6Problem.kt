package aoc.year2015

import DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Rect
import aoc.utils.extensionFunctions.nonEmptyLines
import java.lang.Math.max
import kotlin.time.ExperimentalTime

class Day6Problem : DailyProblem<Int>() {

    override val number = 6
    override val year = 2015
    override val name = "Probably a Fire Hazard"

    enum class Action {
        On, Off, Toggle
    }

    private lateinit var data: List<Pair<Action, Rect>>
    private lateinit var arrayPart1: Array2D<Boolean>
    private lateinit var arrayPart2: Array2D<Int>
    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { line ->
            val coordsRe = """ (\d+),(\d+) through (\d+),(\d+)""".toRegex()
            val match = coordsRe.find(line)!!
            var (corner1x, corner1y, corner2x, corner2y) = match.destructured.toList().map { it.toInt()}
            val op = when(line.substringBefore(match.value)) {
                "turn on" -> Action.On
                "turn off" -> Action.Off
                "toggle" -> Action.Toggle
                else -> { throw Exception("Bad input")}
            }
            if (corner1x > corner2x) {
                val temp = corner1x
                corner1x=corner2x
                corner2x=temp
            }
            if (corner1y > corner2y) {
                val temp = corner1y
                corner1y=corner2y
                corner2y=temp
            }
            Pair(op, Rect(Coord(corner1x, corner1y), Coord(corner2x, corner2y)))
        }
        arrayPart1 = Array2D(List(1000*1000) { false }, 1000,1000)
        arrayPart2 = Array2D(List(1000*1000) { 0 }, 1000,1000)
    }


    override fun part1(): Int {
        data.forEach { (op, rect) ->
            when(op) {
                Action.On -> arrayPart1[rect] = true
                Action.Off -> arrayPart1[rect] = false
                Action.Toggle -> arrayPart1.modifyArea(rect, { !it })
            }
        }
        return arrayPart1.countIndexedByCoordinate { coord, b -> b }
    }


    override fun part2(): Int {
        data.forEach { (op, rect) ->
            when(op) {
                Action.On -> arrayPart2.modifyArea(rect, { it + 1 })
                Action.Off -> arrayPart2.modifyArea(rect, { max(0, it - 1) })
                Action.Toggle -> arrayPart2.modifyArea(rect, { it + 2 })
            }
        }
        return arrayPart2.mapAndFilterToListByNotNull { c, value -> value }.sumOf { it }
    }
}

val day6Problem = Day6Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day6Problem.runBoth(100)
}

/*
=== Day 6 : Probably a Fire Hazard ===
part 1: 569999
part 2: 17836115
Average runtime for year 2015 day 6: 585.232999ms based on 1 runs
===========

 */
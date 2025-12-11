package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.allUnorderedPairs
import aoc.utils.parseListOfPairs
import java.lang.Long.parseLong
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime


data class LineSegment(val x: LongRange, val y: LongRange) {
    fun isInside(p: Pair<Long, Long>) = p.first in x && p.second in y
}

class Day09Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2025
    override val name = "Problem name"

    private lateinit var corners: List<Pair<Long, Long>>
    private lateinit var lines: List<LineSegment>
    private lateinit var cornerXs: Set<Long>
    private lateinit var cornerYs: Set<Long>
    private lateinit var cornerPairsByArea: List<Pair<Pair<Long, Long>, Pair<Long, Long>>>

    override fun commonParts() {
        corners = parseListOfPairs(getInputText(), ::parseLong, ::parseLong, ",")
        lines = buildList {
            corners.windowed(2).forEach { (a, b) -> add(LineSegment(a.first..b.first, a.second..b.second)) }
            val a = corners.first()!!
            val b = corners.last()!!
            add(LineSegment(a.first..b.first, a.second..b.second))
        }
        cornerXs = corners.map { it.first }.toSet()
        cornerYs = corners.map { it.second }.toSet()

        cornerPairsByArea = corners.allUnorderedPairs().sortedByDescending { (c1, c2) ->
            (1 + c1.first - c2.first).absoluteValue * (1 + c1.second - c2.second).absoluteValue
        }.toList()
    }


    override fun part1(): Long {
        return cornerPairsByArea.first()
            .let { (c1, c2) -> (1 + c1.first - c2.first).absoluteValue * (1 + c1.second - c2.second).absoluteValue }
    }

    fun checkInside(c: Pair<Long, Long>): Boolean {
        // return true if on a line segment between corners
        // if not, move rightwards to the next cornerXs position and check again
        // repeat until we are on a line segment or c.x > max(cornersXs) in which case we are outside

        var x: Long? = c.first
        while (x !in cornerXs) {
            x = cornerXs.filter { it > x!! }.minByOrNull { it }
            if (x == null) return false
            if (lines.any { line -> line.isInside(x to c.second) }) return true
        }

        return true


    }

    override fun part2(): Long {
        return 1L
    }
}

val day09Problem = Day09Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day09Problem.testData = false
    day09Problem.runBoth(100)
}
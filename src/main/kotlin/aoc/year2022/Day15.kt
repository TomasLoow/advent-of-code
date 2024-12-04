package aoc.year2022

import DailyProblem
import aoc.utils.Coord
import aoc.utils.nonEmptyLines
import aoc.utils.totalLengthOfCovered
import java.lang.Integer.parseInt
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime

class Day15Problem : DailyProblem<Long>() {

    override val number = 15
    override val year = 2022
    override val name = "Beacon Exclusion Zone"

    var line: Int = 2_000_000
    var size: Int = 4_000_000

    private lateinit var data: List<Pair<Coord, Coord>>

    override fun commonParts() {
        fun parseXYCoord(s: String): Coord {
            val (x, y) = s.drop(2).split(", y=").map(::parseInt)
            return Coord(x, y)
        }

        data = getInputText().nonEmptyLines().map {
            val (sensor, beacon) = it.substringAfter("Sensor at ").split(": closest beacon is at ")
            Pair(parseXYCoord(sensor), parseXYCoord(beacon))
        }
    }

    private fun pointsWithNoBeaconOnLine(sensor: Coord, beacon: Coord, line: Int): IntRange {
        val dist = sensor.manhattanDistanceTo(beacon)
        val hdist = (sensor.y - line).absoluteValue
        val h = dist - hdist
        if (h < 0) return IntRange.EMPTY
        val res = (sensor.x - h..sensor.x + h)
        return if (beacon.y == line) {
            if (beacon.x == res.first) (res.first + 1..res.last) else (res.first until res.last)
        } else {
            res
        }
    }

    override fun part1(): Long {
        val ranges = data.map { (sensor, beacon) ->
            pointsWithNoBeaconOnLine(sensor, beacon, line)
        }.filter { !it.isEmpty() }
        return ranges.totalLengthOfCovered().toLong()
    }

    override fun part2(): Long {
        val badRhombuses = data.map { (sensor, beacon) ->
            val r = Rhombus(sensor, beacon)
            r
        }

        val gigaRhombus = Rhombus((Int.MIN_VALUE..Int.MAX_VALUE), (Int.MIN_VALUE..Int.MAX_VALUE))

        val allowed = badRhombuses.fold(listOf(gigaRhombus)) { allowed, bad ->
            allowed.flatMap { it - bad }
        }

        val c = allowed.flatMap { it.coordsInRect(0, size, 0, size) }.single()
        return (c.x.toLong() * 4000000 + c.y.toLong())
    }
}

class Rhombus {
    private var mUp: IntRange
    private var mDown: IntRange
    constructor(mUp: IntRange, mDown: IntRange) {
        this.mUp = mUp
        this.mDown = mDown
    }

    constructor(sensor: Coord, beacon: Coord) {
        val d = (sensor.x - beacon.x).absoluteValue + (sensor.y - beacon.y).absoluteValue
        mUp = sensor.y - sensor.x - d..sensor.y - sensor.x + d
        mDown = sensor.y + sensor.x - d..sensor.y + sensor.x + d
    }

    private val left: Coord
        get() = coordFromSlopes(mDown.first, mUp.last)

    private val right: Coord
        get() = coordFromSlopes(mDown.last, mUp.first)

    private val top: Coord
        get() = coordFromSlopes(mDown.first, mUp.first)

    private val bottom: Coord
        get() = coordFromSlopes(mDown.last, mUp.last)

    private fun coordFromSlopes(mDownward: Int, mUpward: Int): Coord {
        val step = (mDownward - mUpward) / 2
        return Coord(step, mUpward + step)
    }

    operator fun contains(c: Coord): Boolean {
        return (c.y - c.x in mUp) && (c.y + c.x in mDown)
    }

    operator fun minus(other: Rhombus): List<Rhombus> {
        val (upGood1, upBad, upGood2) = this.mUp.cut(other.mUp)
        val (downGood1, downBad, downGood2) = mDown.cut(other.mDown)
        return buildList {
            if (upGood1 != null && downGood1 != null) add(Rhombus(upGood1, downGood1))
            if (upGood1 != null && downBad   != null) add(Rhombus(upGood1, downBad))
            if (upGood1 != null && downGood2 != null) add(Rhombus(upGood1, downGood2))
            if (upBad   != null && downGood2 != null) add(Rhombus(upBad, downGood2))
            if (upGood2 != null && downGood2 != null) add(Rhombus(upGood2, downGood2))
            if (upGood2 != null && downBad   != null) add(Rhombus(upGood2, downBad))
            if (upGood2 != null && downGood1 != null) add(Rhombus(upGood2, downGood1))
            if (upBad   != null && downGood1 != null) add(Rhombus(upBad, downGood1))
        }
    }

    fun coordsInRect(
        minX: Int = 0,
        maxX: Int = 4_000_000,
        minY: Int = 0,
        maxY: Int = 4_000_000
    ): List<Coord> {
        if (left.x > maxX) return emptyList()
        if (right.x < minX) return emptyList()
        if (top.y < minY) return emptyList()
        if (bottom.y > maxY) return emptyList()
        return (max(minX, left.x)..min(maxX, right.x)).flatMap { x ->
            val l = max(max(minY, mUp.first + x), min(maxY, mDown.first - x))
            val u = min(mUp.last + x, mDown.last - x)
            (l..u).map { y -> Coord(x, y) }
        }
    }

}

private fun IntRange.cut(badArea: IntRange): Triple<IntRange?, IntRange?, IntRange?> {

    if (this.first > badArea.last || this.last < badArea.first) {
        return Triple(this, null, null)  //No overlap
    }
    if (this.first in badArea && this.last in badArea) {
        return Triple(null, this, null)  // fully contained by bad Area
    }

    if (badArea.first in this && badArea.last in this) {
        return Triple(
            (this.first until badArea.first),
            badArea,
            (badArea.last + 1..this.last)
        )  // Bad area fully contained
    }
    if (badArea.last in this) {
        return Triple(null, this.first..badArea.last, badArea.last + 1..this.last)
    }
    if (badArea.first in this) {
        return Triple(this.first until badArea.first, badArea.first..this.last, null)
    }
    throw Exception("Tankevurpa!")
}



val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.runBoth(50)
}

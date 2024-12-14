package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.math.pow
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

private data class Robot(val c: Coord, val velocity: Vector)

class Day14Problem : DailyProblem<Int>() {

    override val number = 14
    override val year = 2024
    override val name = "Restroom Redoubt"

    private lateinit var initialRobots: List<Robot>
    private var mapWidth by Delegates.notNull<Int>()
    private var mapHeight by Delegates.notNull<Int>()

    override fun commonParts() {
        fun parseRobot(s: String): Robot {
            val (ps, vs) = s.split(" ")
            val (x, y) = ps.removePrefix("p=").split(",").map { it.toInt() }
            val (vx, vy) = vs.removePrefix("v=").split(",").map { it.toInt() }
            return Robot(Coord(x, y), Vector(vx, vy))
        }
        initialRobots = getInputText().nonEmptyLines().map { parseRobot(it) }

        if (!testData) {
            mapWidth = 101
            mapHeight = 103
        } else {
            mapWidth = 11
            mapHeight = 7
        }
    }


    override fun part1(): Int {
        val movedRobots = initialRobots.map { moveRobot(it, 100) }
        val factor = safetyFactor(movedRobots)
        return factor
    }

    private fun moveRobot(robot: Robot, steps: Int): Robot {
        val (unboundedX, unboundedY) = robot.c + steps * robot.velocity
        val newPos = Coord(unboundedX.mod(mapWidth), unboundedY.mod(mapHeight))
        return robot.copy(c = newPos)
    }

    private fun safetyFactor(robots: List<Robot>): Int {
        val xMid = mapWidth / 2
        val yMid = mapHeight / 2

         var c1 = 0
         var c2 = 0
         var c3 = 0
         var c4 = 0
         robots.forEach {
             if (it.c.x < xMid && it.c.y < yMid) c1++
             if (it.c.x > xMid && it.c.y < yMid) c2++
             if (it.c.x < xMid && it.c.y > yMid) c3++
             if (it.c.x > xMid && it.c.y > yMid) c4++
        }
        val factor = c1 * c2 * c3 * c4
        return factor
    }

    override fun part2(): Int {
        (1..100_000).forEach { i ->
            val movedRobots = initialRobots.map { moveRobot(it, i) }
            val variance = calculateVariance(movedRobots)
            if (variance < 1000 && movedRobots.map { it.c }.toSet().size == initialRobots.size) {
                val rendered = show(movedRobots)
                if ("1111111111111111111111111111111" in rendered) {
                    //println(rendered)
                    return i
                }
            }
        }
        return 1
    }

    private fun calculateVariance(moved: List<Robot>): Double {
        val xs = emptyMutableList<Int>()
        val ys = emptyMutableList<Int>()
        var xSum = 0
        var ySum = 0
        moved.forEach { (c,_) ->
            xs.add(c.x)
            ys.add(c.y)
            xSum += c.x
            ySum += c.y
        }
        val xavg = xSum.toDouble() / xs.size
        val yavg = ySum.toDouble() / xs.size
        val xvariance = xs.sumOf { (it - xavg).pow(2) } / xs.size
        val yvariance = ys.sumOf { (it - yavg).pow(2) } / ys.size
        val variance = xvariance + yvariance
        return variance
    }

    private fun show(robots: List<Robot>): String {
        val a = Array2D<Int>(mapWidth, mapHeight) { _ -> 0 }
        robots.forEach { a[it.c] = a[it.c] + 1 }
        return a.show { if (it == 0) "." else it.toString() }
    }
}

val day14Problem = Day14Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    //day14Problem.testData = true
    day14Problem.runBoth(100)
}
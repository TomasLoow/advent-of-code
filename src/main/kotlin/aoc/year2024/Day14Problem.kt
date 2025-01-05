package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.variance
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Vector
import aoc.utils.geometry.times
import aoc.utils.math.chineseRemainder
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
        // The target image has a very low variance in both the x and y coordinates
        // The x positions loop with a cycle of mapWidth. Find index where the x positions have the lowest variance
        val idxVarMinX = (0..mapWidth).map { idx ->
            val movedRobots = initialRobots.map { robot -> moveRobot(robot, idx) }
            idx to movedRobots.map { it.c.x }.variance()
        }.minByOrNull { it.second }!!.first

        // The y positions loop with a cycle of mapHeight. Find index where the y positions have the lowest variance
        val idxVarMinY = (0..mapHeight).map { idx ->
            val movedRobots = initialRobots.map { robot -> moveRobot(robot, idx) }
            idx to movedRobots.map { it.c.y }.variance()
        }.minByOrNull { it.second }!!.first

        // Find x such that x = idxVarMinX (mod) mapWidth and  x = idxVarMinY (mod) mapHeight using the Chinese Remainder Theorem
        val x = chineseRemainder(
            listOf(
                mapWidth to idxVarMinX,
                mapHeight to idxVarMinY
            )
        )
        return x
    }

    @Suppress("unused")
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
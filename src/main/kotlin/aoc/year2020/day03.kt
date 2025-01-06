package aoc.year2020

import DailyProblem
import aoc.utils.extensionFunctions.productL
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import kotlin.time.ExperimentalTime

class Day03Problem : DailyProblem<Long>() {

    override val number = 3
    override val year = 2020
    override val name = "Toboggan Trajectory"

    private lateinit var data: Array2D<Boolean>

    override fun commonParts() {
        data = Array2D.parseFromLines(getInputText()) { it == '#' }
    }

    fun run(dx: Int, dy: Int): Int {
        var x = 0
        var y = 0
        var count = 0
        while (y < data.height - 1) {
            if (data[x,y]) count++
            x = (x + dx) % data.width
            y += dy
        }
        if (data[x,y]) count++
        return count
    }

    override fun part1(): Long {
        val dx = 3
        val dy = 1
        return run(dx, dy).toLong()
    }


    override fun part2(): Long {

        val trajectories = listOf(
            (1 to 1),
            (3 to 1),
            (5 to 1),
            (7 to 1),
            (1 to 2),
        )

        return trajectories.map { run(it.first, it.second) }.productL()
    }
}

val day03Problem = Day03Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day03Problem.testData = false
    day03Problem.runBoth(100)
}
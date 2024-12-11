package aoc.year2024

import DailyProblem
import aoc.utils.even
import aoc.utils.parseOneLineOfSeparated
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime

class Day11Problem : DailyProblem<Long>() {

    override val number = 11
    override val year = 2024
    override val name = "Plutonian Pebbles"

    private lateinit var data: List<Int>

    override fun commonParts() {
        data = parseOneLineOfSeparated(getInputText(), ::parseInt, " ")
    }

    private fun evolve(x: Long) : List<Long> {
        if (x == 0L) {
            return listOf(1L)
        }
        val asString = x.toString()
        val length = asString.length
        if (length.even) {
            return listOf(
                asString.take(length/2).toLong(),
                asString.drop(length/2).toLong(),
            )
        }
        return listOf(x*2024)
    }


    override fun part1(): Long {
        return solve(25)
    }

    override fun part2(): Long {
        return solve(75)
    }

    private fun solve(steps: Int): Long {
        var counters = mutableMapOf<Long, Long>()
        data.forEach {
            val c = counters.getOrDefault(it.toLong(), 0L)
            counters[it.toLong()] = c+1
        }

        repeat(steps) {
            val currentCounts = counters.toList()
            counters.clear()
            currentCounts.forEach { (stone, count) ->
                evolve(stone).forEach { newStone ->
                    val current = counters.getOrDefault(newStone, 0L)
                    counters[newStone] = current+count
                }
            }
        }
        return counters.map{ it.value }.sum()
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.testData = false
    day11Problem.runBoth(10)
}
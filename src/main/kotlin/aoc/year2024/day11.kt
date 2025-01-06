package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.increase
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


    private fun evolve(x: Long): List<Long> {
        // If it looks really stupid but works and is quite fast, is it actually stupid?
        if (x == 0L) return listOf(1L)
        if (x < 10L) return listOf(x * 2024L)
        if (x < 100L) return listOf(x / 10L, x % 10L)
        if (x < 1000L) return listOf(x * 2024L)
        if (x < 10000L) return listOf(x / 100L, x % 100L)
        if (x < 100000L) return listOf(x * 2024L)
        if (x < 1000000L) return listOf(x / 1000L, x % 1000L)
        if (x < 10000000L) return listOf(x * 2024L)
        if (x < 100000000L) return listOf(x / 10000L, x % 10000L)
        if (x < 1000000000L) return listOf(x * 2024L)
        if (x < 10000000000L) return listOf(x / 100000L, x % 100000L)
        if (x < 100000000000L) return listOf(x * 2024L)
        if (x < 1000000000000L) return listOf(x / 1000000L, x % 1000000L)
        if (x < 10000000000000L) return listOf(x * 2024L)
        if (x < 100000000000000L) return listOf(x / 10000000L, x % 10000000L)
        throw Exception("What? Even BIGGER?!")
    }


    override fun part1(): Long {
        return solve(25)
    }

    override fun part2(): Long {
        return solve(75)
    }

    private fun solve(steps: Int): Long {
        val counters = mutableMapOf<Long, Long>()
        data.forEach {
            counters.increase(it.toLong(), 1)
        }
        repeat(steps) {
            val currentCounts = counters.toList()
            counters.clear()
            currentCounts.forEach { (stone, count) ->
                evolve(stone).forEach { counters.increase(it, count) }
            }
        }
        return counters.map { it.value }.sum()
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.testData = false
    day11Problem.runBoth(1000)
}
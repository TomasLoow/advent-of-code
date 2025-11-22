package aoc.year2020

import aoc.DailyProblem
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day10Problem : DailyProblem<Long>() {

    override val number = 10
    override val year = 2020
    override val name = "Adapter Array"

    private lateinit var data: List<Int>

    override fun commonParts() {
        val input = parseIntLines(getInputText()).sorted()
        data = listOf(0) + input + (input.maxOrNull()!! + 3)  // Add outlet and final adapter
    }


    override fun part1(): Long {
        val diffs = data.zipWithNext().map { it.second - it.first }
        val ones = diffs.count { it == 1 }.toLong()
        val threes = diffs.count { it == 3 }.toLong()
        return ones * threes
    }

    private fun countWays(l: List<Int>, cache: MutableMap<Int, Long>): Long {
        if (l.size in cache) return cache[l.size]!!
        if (l.size == 1) return 1
        val h = l.first()
        val step1 = if (h + 1 == l[1]) countWays(l.drop(1), cache) else 0L
        val step2 = if (h + 2 in l.take(4)) countWays(l.dropWhile { it != h + 2 }, cache) else 0L
        val step3 = if (h + 3 in l.take(4)) countWays(l.dropWhile { it != h + 3 }, cache) else 0L
        val count = step1 + step2 + step3
        cache[l.size] = count
        return count
    }

    override fun part2(): Long {
        val cache = mutableMapOf<Int, Long>()
        return countWays(data, cache)
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.testData = false
    day10Problem.runBoth(100)
}
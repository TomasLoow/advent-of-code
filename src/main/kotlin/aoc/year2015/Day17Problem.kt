package aoc.year2015

import DailyProblem
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day17Problem : DailyProblem<Int>() {

    override val number = 17
    override val year = 2015
    override val name = "No Such Thing as Too Much"

    var target: Int = 150

    private lateinit var data: List<Int>
    private lateinit var combinations: List<List<Int>>

    override fun commonParts() {
        data = parseIntLines(getInputText())
        combinations = getCombinations(data, target)
    }

    fun getCombinations(set: List<Int>, target: Int): List<List<Int>> {
        if (target == 0) return listOf(listOf())
        if (set.size == 1) {
            return if (set.first() == target) listOf(listOf(target)) else listOf()
        }
        val current = set.first()
        val setWithout = set.drop(1)

        return getCombinations(setWithout, target) + getCombinations(setWithout, target-current).map { it + current }
    }

    override fun part1(): Int {
        return combinations.size
    }


    override fun part2(): Int {
        val minSize = combinations.minOf { it.size }
        return combinations.count { it.size == minSize }
    }
}

val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.runBoth(100)
}
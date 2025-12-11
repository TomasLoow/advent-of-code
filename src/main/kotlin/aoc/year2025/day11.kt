package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day11Problem : DailyProblem<Long>() {
    override val number = 11
    override val year = 2025
    override val name = "Reactor"

    private lateinit var links: Map<String,List<String>>

    override fun commonParts() {
        links = getInputText().nonEmptyLines().map { line ->
            val (from, linked) = line.split(": ")
            from to linked.split(" ")
        }.toMap()
    }


    override fun part1(): Long {
        return countPaths("you", "out", emptyList())
    }

    private fun countPaths(from: String, goal: String, forbidden:List<String>): Long {
        val memo = mutableMapOf<String, Long>()
        fun inner(from: String): Long {
            if (memo.containsKey(from)) return memo[from]!!
            if (from == goal) return 1
            if (from !in links) return 0
            val res = links[from]!!.map { next ->
                when (next) {
                    in forbidden -> 0 // DON'T GO THERE
                    else -> inner(next)
                }
            }.sum()
            memo[from] = res
            return res
        }
        return inner(from)
    }



    override fun part2(): Long {
        val s2d = countPaths("svr", "dac", listOf("fft"))
        val d2f = if (s2d == 0L) 0 else countPaths("dac", "fft", listOf("dac"))
        val f2o = if (d2f == 0L) 0 else countPaths("fft", "out", listOf("dac", "fft"))
        val pathsInOrderDF = s2d * d2f * f2o

        val s2f = countPaths("svr", "fft", listOf("dac"))
        val f2d = if (s2f == 0L) 0L else countPaths("fft", "dac", listOf("fft"))
        val d2o = if (f2d == 0L) 0L else countPaths("dac", "out", listOf("dac", "fft"))
        val pathsInOrderFD = s2f * f2d * d2o

        return pathsInOrderDF + pathsInOrderFD
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.testData = false
    day11Problem.runBoth(100)
}
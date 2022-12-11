package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.math.max
import kotlin.math.min
import kotlin.time.ExperimentalTime

class Day9Problem() : DailyProblem<Int>() {

    override val number = 9
    override val year = 2015
    override val name = "All in a Single Night"
    private lateinit var distancesMap: Map<String, MutableMap<String, Int>>
    private lateinit var solution: Pair<Int, Int>

    override fun commonParts() {
        val distances = parseListOfTriples(getInputText(), ::id, ::id, ::parseInt, " to ", " = ")
        val places = distances.flatMap { listOf(it.first, it.second) }.toSet().toList()
        distancesMap = buildMap {
            places.forEach { place -> this[place] = mutableMapOf<String, Int>() }
            distances.forEach { (a, b, d) ->
                this[a]!![b] = d
                this[b]!![a] = d
            }
        }
        solution = places.permutationsSequence()
            .filter { perm -> perm.first() < perm.last() } // by dropping half we avoid checking same route in both direction!
            .fold(Pair(Int.MAX_VALUE, Int.MIN_VALUE)) { state, perm ->
                val (prevMin, prevMax) = state
                val score = scoreRoute(perm)
                Pair(min(prevMin, score), max(prevMax, score))
            }
    }

    private fun scoreRoute(route: List<String>): Int {
        return route.windowed(2).sumOf { (a, b) -> distancesMap[a]!![b]!! }
    }

    override fun part1(): Int {
        /* there are only eight places, brute forcing is an option, only 40320 permutations to check! */
        return solution.first
    }

    override fun part2(): Int {
        return solution.second
    }
}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.runBoth(100)
}
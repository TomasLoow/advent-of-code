package aoc.year2024

import aoc.DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day19Problem : DailyProblem<Long>() {

    override val number = 19
    override val year = 2024
    override val name = "Linen Layout"

    private lateinit var patterns: List<String>
    private lateinit var goals: List<String>
    private lateinit var counts: List<Long>

    override fun commonParts() {
        parseTwoBlocks(
            getInputText(),
            { parseOneLineOfSeparated(it, ::id, ", ") },
            String::nonEmptyLines
        ).let { (p, g) ->
            patterns = p
            goals = g
        }

        val cache: MutableMap<String, Long> = mutableMapOf("" to 1)
        counts = goals.map { countWaysToMakeDesign(it, cache) } // Use this for both 1 and 2
    }

    /** Simple recursive count with a cache of already calculated substrings */
    private fun countWaysToMakeDesign(goal: String, cache: MutableMap<String, Long>): Long {
        if (goal.isEmpty()) return 1
        if (goal in cache) return cache[goal]!!
        val prefixes: List<String> = patterns.filter { goal.startsWith(it) }
        if (prefixes.isEmpty()) return 0
        val res = prefixes.sumOf { countWaysToMakeDesign(goal.removePrefix(it), cache) }
        cache[goal] = res
        return res
    }

    override fun part1(): Long {
        return counts.count { it != 0L }.toLong()
    }

    override fun part2(): Long {
        return counts.sumOf { it }
    }
}

val day19Problem = Day19Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day19Problem.testData = false
    day19Problem.runBoth(50)
}
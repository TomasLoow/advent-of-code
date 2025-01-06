package aoc.year2024

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.increase
import aoc.utils.parseListOfPairs
import java.lang.Integer.parseInt
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {
    override val number = 1
    override val year = 2024
    override val name = "Historian Hysteria"

    private lateinit var listLeft : List<Int>
    private lateinit var listRight : List<Int>

    override fun commonParts() {
        val lists  = parseListOfPairs(getInputText(), ::parseInt, ::parseInt, Regex(" +")).unzip()
        listLeft = lists.first
        listRight = lists.second
    }

    override fun part1(): Int {
        return listLeft.sorted().zip(listRight.sorted()).sumOf { (l, r) ->
            (l - r).absoluteValue
        }
    }

    override fun part2(): Int {
        val rCounts = emptyMutableMap<Int, Int>()
        listRight.forEach { r -> rCounts.increase(r, 1) }
        return listLeft.sumOf { l ->
            l * rCounts.getOrDefault(l, 0)
        }
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.runBoth(100)
}



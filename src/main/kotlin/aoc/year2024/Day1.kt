package aoc.year2024

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.parseListOfPairs
import java.lang.Integer.parseInt
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

class Day1Problem : DailyProblem<Int>() {
    override val number = 1
    override val year = 2024
    override val name = "Historian Hysteria"

    private lateinit var listLeft : MutableList<Int>
    private lateinit var listRight : MutableList<Int>

    override fun commonParts() {
        val listOfPairs  = parseListOfPairs(getInputText(), ::parseInt, ::parseInt, Regex(" +"))
        listLeft = mutableListOf()
        listRight = mutableListOf()
        listOfPairs.forEach { (i1, i2) ->
            listLeft.add(i1)
            listRight.add(i2)
        }
        listLeft.sort()
        listRight.sort()
    }

    override fun part1(): Int {
        return listLeft.zip(listRight).map { (l,r) ->
            (l - r).absoluteValue
        }.sum()
    }

    override fun part2(): Int {
        val rCounts = emptyMutableMap<Int, Int>()
        listRight.forEach { r ->
            rCounts[r] = rCounts.getOrDefault(r, 0) + 1
        }
        return listLeft.sumOf { l ->
            l * rCounts.getOrDefault(l, 0)
        }
    }
}

val day1Problem = Day1Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day1Problem.runBoth(100)
}



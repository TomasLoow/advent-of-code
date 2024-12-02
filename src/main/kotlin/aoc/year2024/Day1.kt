package aoc.year2024

import DailyProblem
import aoc.utils.parseBlockList
import aoc.utils.parseIntLines
import aoc.utils.parseListOfPairs
import java.lang.Integer.parseInt
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

class Day1Problem() : DailyProblem<Int>() {
    private lateinit var caloriesList: List<List<Int>>

    override val number = 1
    override val year = 2024
    override val name = "Historian Hysteria"

    private lateinit var listOfPairs: List<Pair<Int, Int>>
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    override fun commonParts() {
        listOfPairs  = parseListOfPairs(getInputText(), ::parseInt, ::parseInt, Regex(" +"))
        listOfPairs.forEach { (i1, i2) ->
            list1.add(i1)
            list2.add(i2)
        }
    }

    override fun part1(): Int {
        return list1.sorted().zip(list2.sorted()).map {
            (it.first - it.second).absoluteValue
        }.sum()
    }

    override fun part2(): Int {
        return list1.map { l ->
            l*list2.count { r -> r == l }
        }.sum()
    }
}


val day1Problem = Day1Problem()

fun main() {
    day1Problem.commonParts()
    println(day1Problem.part2())
}



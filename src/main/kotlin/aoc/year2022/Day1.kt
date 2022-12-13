package aoc.year2022

import DailyProblem
import aoc.utils.parseBlockList
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day1Problem() : DailyProblem<Int>() {
    private lateinit var caloriesList: List<List<Int>>

    override val number = 1
    override val year = 2022
    override val name = "Calorie Counting"

    override fun commonParts() {
        caloriesList = parseBlockList(getInputText(), ::parseIntLines)
    }

    override fun part1(): Int {
        return caloriesList.maxOf { it.sum() }
    }

    override fun part2(): Int {
        val topThree = caloriesList.sortedByDescending { it.sum() }.take(3)
        return topThree.map { it.sum() }.sumOf { it }
    }
}


val day1Problem = Day1Problem()



@OptIn(ExperimentalTime::class)
fun main() {
    println(day1Problem.runBoth(100))
}



package aoc.year2022

import aoc.DailyProblem
import aoc.utils.parseBlockList
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {
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
        return topThree.sumOf { it.sum() }
    }
}


val day01Problem = Day01Problem()



@OptIn(ExperimentalTime::class)
fun main() {
    println(day01Problem.runBoth(100))
}



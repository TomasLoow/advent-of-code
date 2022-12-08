package aoc2022

import DailyProblem
import utils.parseBlockList
import utils.parseIntLines

class Day1Problem(override val inputFilePath: String) : DailyProblem<Int> {
    private lateinit var caloriesList: List<List<Int>>

    override val number = 1

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


val day1Problem = Day1Problem("input/aoc2022/day1.txt")



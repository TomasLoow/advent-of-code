package aoc2022

import DailyProblem
import utils.ensureNl
import utils.nonEmptyLines
import java.io.File

class Day1Problem(override val inputFilePath: String) : DailyProblem<Int> {
    private lateinit var caloriesList: List<List<Int>>

    override val number = 1

    override val name = "Calorie Counting"

    fun parseIntsGroupsFile(path: String): List<List<Int>> = File(path)
        .readText().ensureNl()
        .split("\n\n")
        .map { group ->
            group.nonEmptyLines().map { line -> line.toInt() }
        }

    override fun commonParts() {
        caloriesList = parseIntsGroupsFile(inputFilePath)
    }

    override fun part1(): Int {
        val max = caloriesList.maxOf { it.sum() }
        return max
    }

    override fun part2(): Int {
        val topThree = caloriesList.sortedByDescending { it.sum() }.take(3)
        return topThree.map { it.sum() }.sumOf { it }
    }
}


val day1Problem = Day1Problem("input/aoc2022/day1.txt")



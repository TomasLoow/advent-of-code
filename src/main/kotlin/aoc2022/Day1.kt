package aoc2022

import DailyProblem
import java.io.File

class Day1Problem(override val inputFilePath: String) : DailyProblem {
    private lateinit var caloriesList: List<List<Int>>

    override val number = 1

    override val name = "Calorie Counting"

    fun parseIntsGroupsFile(path: String): List<List<Int>> = File(path)
        .readText()
        .split("\n\n")
        .map { group ->
            group.lines().filter { it.isNotEmpty() }.map { line -> line.toInt() }
        }

    override fun commonParts() {
        caloriesList = parseIntsGroupsFile(inputFilePath)
    }

    override fun part1(): Long {
        val max = caloriesList.maxOf { it.sum() }
        return max.toLong()
    }

    override fun part2(): Long {
        val topThree = caloriesList.sortedByDescending { it.sum() }.take(3)
        return topThree.map { it.sum() }.sumOf { it }.toLong()
    }
}

val day1Problem = Day1Problem("input/aoc2022/day1.txt")



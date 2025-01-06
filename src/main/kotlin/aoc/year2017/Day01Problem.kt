package aoc.year2017

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.rotateRight
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {

    override val number = 1
    override val year = 2017
    override val name = "Inverse Captcha"

    private lateinit var data: List<List<Int>>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { it.map { it.digitToInt()} }
    }


    override fun part1(): Int {
        return data.sumOf { line ->
            line.zip(line.rotateRight(1)).filter { it.first == it.second }.map { it.first }.sum()
        }
    }


    override fun part2(): Int {
        return data.sumOf { line ->
            val offset = line.size / 2
            line.zip(line.rotateRight(offset)).filter { it.first == it.second }.map { it.first }.sum()
        }
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.testData = false
    day01Problem.runBoth(1)
}
package aoc2022

import DailyProblem
import utils.nonEmptyLines
import java.io.File
import kotlin.time.ExperimentalTime

class Day999999Problem(override val inputFilePath: String) : DailyProblem<Long> {

    override val number = 999999
    override val name = "Problem name"

    private fun parseFile(): Any {
        val input = """""".lines()
        // val input = File(inputFileName).readLines()
        TODO()
    }



    override fun part1(): Long {
        val data = parseFile()
        return 1L
    }


    override fun part2(): Long {
        val data = parseFile()
        return 1L
    }
}

val day999999Problem = Day999999Problem("input/aoc2022/day999999.txt")

@OptIn(ExperimentalTime::class)
fun main() {
    day999999Problem.runBoth(100)
}
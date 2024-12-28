package aoc.year2023

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime


private val replacementTable = mapOf(
    "one" to "o1e",
    "three" to "t3e",
    "seven" to "7n",
    "two" to "t2o",
    "five" to "5e",
    "four" to "4",
    "six" to "6",
    "nine" to "n9e",
    "eight" to "e8t",
)

private fun digitize(s:String) : String {
    var tmp = s
    for ((k, v) in replacementTable) {
        tmp = tmp.replace(k, v)
    }
    return tmp
}

class Day1Problem : DailyProblem<Int>() {
    override val number = 1
    override val year = 2023
    override val name = "Trebuchet?!"

    private lateinit var lines: List<String>

    override fun commonParts() {
        lines = getInputText().nonEmptyLines()
    }

    override fun part1(): Int {
        return lines.sumOf { line ->
            val digits = line.filter { it.isDigit() }.map { parseInt(it.toString()) }
            digits.first() * 10 + digits.last()
        }
    }

    override fun part2(): Int {
        return lines.sumOf { line ->
            val digits = digitize(line).filter { it.isDigit() }.map { parseInt(it.toString()) }
            digits.first() * 10 + digits.last()
        }
    }
}


val day1Problem = Day1Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    println(day1Problem.runBoth(timesToRun = 100))
}



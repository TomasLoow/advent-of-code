package aoc.year2020

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day02Problem : DailyProblem<Int>() {

    override val number = 2
    override val year = 2020
    override val name = "Password Philosophy"

    private data class Rule(val low: Int, val high: Int, val letter: Char)

    private lateinit var data: List<Pair<Rule, String>>

    override fun commonParts() {
        fun parse(line: String): Pair<Rule, String> {
            val (r, c, s) = line.split(" ")
            val (l, h) = r.split("-")
            val rule = Rule(l.toInt(), h.toInt(), c[0])
            return rule to s
        }
        data = getInputText().nonEmptyLines().map {
            parse(it)
        }

    }

    private fun validPart1(it: Pair<Rule, String>): Boolean {
        val (rule, string) = it
        return string.count { c -> c == rule.letter } in (rule.low..rule.high)
    }

    private fun validPart2(it: Pair<Rule, String>): Boolean {
        val (rule, string) = it
        return listOf(string[rule.low-1], string[rule.high-1]).count { c -> c == rule.letter } == 1
    }


    override fun part1(): Int {
        return data.count {
            validPart1(it)
        }
    }


    override fun part2(): Int {
        return data.count {
            validPart2(it)
        }
    }
}

val day02Problem = Day02Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day02Problem.testData = false
    day02Problem.runBoth(10)
}
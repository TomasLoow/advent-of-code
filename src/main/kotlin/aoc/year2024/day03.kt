package aoc.year2024

import DailyProblem
import kotlin.time.ExperimentalTime

class Day03Problem : DailyProblem<Int>() {

    override val number = 3
    override val year = 2024
    override val name = "Mull It Over"

    private lateinit var data: String
    private val reFindMultiplications = Regex("mul\\((\\d+),(\\d+)\\)")
    private val reFindDoesAndDonts = Regex("do\\(\\)|don't\\(\\)")

    override fun commonParts() {
        data = getInputText()
    }

    private fun evalMultsInString(s: String): Int {
        val matches = reFindMultiplications.findAll(s)
        return matches.map { match ->
            match.groupValues[1].toInt() * match.groupValues[2].toInt()
        }.sum()
    }

    override fun part1(): Int {
        return evalMultsInString(data)
    }


    override fun part2(): Int {
        var matching = true
        var res = 0
        val fixedData = "do()" + data + "don't()"  // modify input string to sidestep edge cases at beginning and end
        (reFindDoesAndDonts.findAll(fixedData).zipWithNext()).forEach { (match, next) ->
            matching = (match.value == "do()")
            if (matching) {
                // everything from here to the next match should be evaluated
                val subStringUpToNextMatch = fixedData.substring(match.range.last + 1..next.range.first)
                res += evalMultsInString(subStringUpToNextMatch)
            }
        }
        return res
    }
}

val day03Problem = Day03Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day03Problem.runBoth(100)
}
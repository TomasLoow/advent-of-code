package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day5Problem : DailyProblem<Int>() {

    override val number = 5
    override val year = 2015
    override val name = "Doesn't He Have Intern-Elves For This?"

    companion object {
        val badStrings = listOf("ab", "cd", "pq", "xy")
    }

    private lateinit var inputStrings: List<String>

    override fun commonParts() {
        inputStrings = getInputText().nonEmptyLines()
    }

    fun isNice1(string: String): Boolean {
        val vowelCount = string.count { c -> c in "aeiou" }
        val hasDuplicate = string.windowed(2).find { pair -> pair[0] == pair[1] } != null
        val containsBadString = badStrings.any { it in string }

        return (vowelCount >= 3) && hasDuplicate && !containsBadString
    }

    fun isNice2(string: String): Boolean {
        val repeatsPair = string.windowed(2).filterIndexed { idx, subString ->
            val foundIdx = string.indexOf(subString, idx+2)
                foundIdx > 0
        }.isNotEmpty()
        val hasSeparatedPair = string.windowed(3).find { pair -> pair[0] == pair[2] } != null

        return repeatsPair && hasSeparatedPair
    }


    override fun part1(): Int {
        return inputStrings.count { line -> isNice1(line) }
    }

    override fun part2(): Int {
        return inputStrings.count { line -> isNice2(line) }
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.testData=true
    day5Problem.runBoth(100)
}
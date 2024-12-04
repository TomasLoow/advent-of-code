package aoc.year2015

import DailyProblem
import kotlin.time.ExperimentalTime

class Day11Problem : DailyProblem<String>() {
    override val number = 11
    override val year = 2015
    override val name = "Corporate Policy"

    private lateinit var startingString: String


    override fun commonParts() {
        startingString = getInputText().trim()
    }

    fun policy1(string: String): Boolean {
        return STRAIGHTS.any { it in string }
    }

    fun policy2(string: String): Boolean {
        return !string.any { c -> c in FORBIDDEN }
    }

    fun policy3(string: String): Boolean {
        return REGEX_TWO_PAIRS.containsMatchIn(string) && !REGEX_TRIPLE.containsMatchIn(string)
    }

    fun policy(string: String) : Boolean =  policy1(string) && policy3(string) && policy2(string)

    fun streamCandidates(startingFrom: String): Sequence<String> {
        var str = startingFrom
        return sequence {
            while (true) {
                val numEndingZs = REGEX_ENDING_ZS.find(str)!!.value.length
                val s2 = str.dropLast(numEndingZs)
                val last = s2.last()
                str = s2.dropLast(1) + incChar(last) + "a".repeat(numEndingZs)
                yield(str)
            }
        }
    }

    private fun incChar(last: Char): Char {
        val c = last + 1
        return if (c in FORBIDDEN) incChar(c) else c    }
    override fun part1(): String {
        return streamCandidates(startingString).filter(::policy).first()
    }

    override fun part2(): String {
        return streamCandidates(startingString).filter(::policy).drop(1).first()
    }

    companion object {
        private val FORBIDDEN = listOf('i', 'o', 'l')
        private val STRAIGHTS = "abcdefghijklmnopqrstuvxyz"
            .windowed(3)
            .filter { straight -> !straight.any{ it in FORBIDDEN} }
        private val REGEX_TWO_PAIRS = """(.)\1.*(.)\2""".toRegex()
        private val REGEX_TRIPLE = """(.)\1\1""".toRegex()
        private val REGEX_ENDING_ZS = """z*$""".toRegex()

    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.runBoth(10)
}
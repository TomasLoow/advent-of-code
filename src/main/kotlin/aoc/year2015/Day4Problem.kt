package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.md5
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day04Problem : DailyProblem<Int>() {

    override val number = 4
    override val year = 2015
    override val name = "The Ideal Stocking Stuffer"

    override fun part1(): Int {
        val inp = getInputText().nonEmptyLines().first()
        return findHash(inp, 5)
    }

    private fun findHash(inp: String, len: Int): Int {
        var c = 0
        val prefix = "0".repeat(len)
        while (true) {
            if ("$inp$c".md5().startsWith(prefix)) {
                return c
            }
            c++
        }
    }


    override fun part2(): Int {
        val inp = getInputText().nonEmptyLines().first()
        return findHash(inp, 6)
    }
}


val day04Problem = Day04Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day04Problem.runBoth(10)
}
package aoc.year2015

import DailyProblem
import aoc.utils.md5
import aoc.utils.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day4Problem : DailyProblem<Int>() {

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


val day4Problem = Day4Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day4Problem.runBoth(10)
}
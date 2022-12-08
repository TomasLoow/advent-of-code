package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day1Problem() : DailyProblem<Int>() {

    override val number = 1
    override val year = 2015
    override val name = "Not Quite Lisp"

    override fun part1(): Int {
        return getInputText().fold(0) { floor, c ->
            when (c) {
                '(' -> floor + 1
                ')' -> floor - 1
                else -> throw Exception("Bad input")
            }
        }
    }


    override fun part2(): Int {
        var floor = 0
        getInputText().forEachIndexed{ idx, c ->
            when (c) {
                '(' -> floor += 1
                ')' -> floor -= 1
                else -> throw Exception("Bad input")
            }
            if (floor < 0) return idx
        }
        throw Exception("Never hit the basement")
    }
}

val day1Problem = Day1Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day1Problem.runBoth(100)
}
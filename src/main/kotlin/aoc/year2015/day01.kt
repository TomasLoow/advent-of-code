package aoc.year2015

import DailyProblem
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Int>() {

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

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.runBoth(100)
}
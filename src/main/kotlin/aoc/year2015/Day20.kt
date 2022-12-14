package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day20Problem() : DailyProblem<Int>() {

    override val number = 20
    override val year = 2015
    override val name = "Infinite Elves and Infinite Houses"

    private var target: Int = 0

    override fun commonParts() {
        target = getInputText().nonEmptyLines().single().toInt() / 10
    }

    override fun part1(): Int {
        val a = Array<Int>(target + 1) { 0 }

        (1..target).forEach { elf ->
            (elf..target step elf).forEach { house ->
                a[house] += elf
            }
        }
        return a.indexOfFirst { it > target }
    }


    override fun part2(): Int {
        val a = Array<Int>(target + 1) { 0 }

        (1..target).forEach { elf ->
            (elf..target step elf).take(50).forEach { house ->
                a[house] += elf * 11
            }
        }
        return a.indexOfFirst { it >= target * 10 }
    }
}

val day20Problem = Day20Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day20Problem.runBoth(10)
}
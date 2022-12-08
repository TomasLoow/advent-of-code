package aoc.year2022

import DailyProblem
import kotlin.time.ExperimentalTime

class Day6Problem() : DailyProblem<Int>() {

    override val number = 6
    override val year = 2022
    override val name = "Tuning Trouble"

    private lateinit var data: String

    private fun parseFile(): String {
        return getInputText()
    }

    override fun commonParts() {
        this.data = parseFile()
    }

    private fun findPackage(size: Int): Int {
        val start = data
            .windowed(size)
            .indexOfFirst {
                it.toSet().size == size
            }
        return start + size  // We want the index of the item *after* the window
    }

    override fun part1(): Int {
        return findPackage(4)
    }


    override fun part2(): Int {
        return findPackage(14)
    }
}

val day6Problem = Day6Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day6Problem.runBoth(100)
}
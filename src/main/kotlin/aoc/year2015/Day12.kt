package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime
import aoc.year2015.crappyJSON.*
class Day12Problem() : DailyProblem<Int>() {
    override val number = 12
    override val year = 2015
    override val name = "JSAbacusFramework.io"

    private lateinit var data: JSON

    override fun commonParts() {
        data = parseJSON(getInputText()).first
    }


    override fun part1(): Int {
        return sumInts(data)
    }

    private fun sumInts(json: JSON): Int {
        return when(json) {
            is JSON.I -> json.int
            is JSON.S -> 0
            is JSON.A -> json.content.sumOf { sumInts(it) }
            is JSON.O -> json.content.values.sumOf { sumInts(it) }
        }
    }

    private fun sumIntsIfNotRed(json: JSON): Int {
        return when(json) {
            is JSON.I -> json.int
            is JSON.S -> 0
            is JSON.A -> json.content.sumOf { sumIntsIfNotRed(it) }
            is JSON.O -> {
                if (json.content.values.any { j -> j is JSON.S && j.string == "red" }) {
                    0
                } else
                {
                    json.content.values.sumOf { sumIntsIfNotRed(it) }
                }
            }
        }
    }


    override fun part2(): Int {
        return sumIntsIfNotRed(data)
    }
}

val day12Problem = Day12Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day12Problem.runBoth(1000)
}
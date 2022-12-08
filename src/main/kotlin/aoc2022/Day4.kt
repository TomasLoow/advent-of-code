package aoc2022

import DailyProblem
import utils.*
import kotlin.time.ExperimentalTime

class Day4Problem(override val inputFilePath: String) : DailyProblem<Int> {

    override val number = 4
    override val name = "Camp Cleanup"

    private lateinit var data: List<Pair<IntRange, IntRange>>

    private fun parseFile(): List<Pair<IntRange, IntRange>> {
        val cleanupLineRegEx = """(\d+)-(\d+),(\d+)-(\d+)""".toRegex()
        return getInputFile()
            .readNonEmptyLines()
            .map { line ->
                val (elf1Start, elf1End, elf2Start, elf2End) = cleanupLineRegEx.matchEntire(line)!!.destructured.toList().map { s -> s.toInt()}
                Pair(
                    IntRange(elf1Start, elf1End),
                    IntRange(elf2Start, elf2End)
                )
            }
    }

    override fun commonParts() {
        this.data = parseFile()
    }

    override fun part1(): Int {
        return data.count { (elf1, elf2) ->
            elf1.containsRange(elf2) || elf2.containsRange(elf1)
        }
    }

    override fun part2(): Int {
        return data.count { (elf1, elf2) ->
            elf1.intersectRange(elf2)
        }
    }
}


val day4Problem = Day4Problem("input/aoc2022/day4.txt")

@OptIn(ExperimentalTime::class)
fun main() {
    day4Problem.runBoth(10)
}
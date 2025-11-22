package aoc.year2024

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseBlockList
import kotlin.time.ExperimentalTime

class Day25Problem : DailyProblem<Int>() {

    override val number = 25
    override val year = 2024
    override val name = "Code Chronicle"

    private lateinit var locks: List<List<Int>>
    private lateinit var keys: List<List<Int>>

    override fun commonParts() {
        /**
         * Returns a list of heights and an isKey boolean
         */
        fun parseKeyOrLock(str: String): Pair<List<Int>,Boolean> {
            val lines = str.nonEmptyLines()
            val isKey = lines.first().all { c-> c=='#'}
            val counts = (0..4).map { col ->
                lines.count { it[col] == '#'}
            }
             return Pair(counts, isKey)
        }

        val blocks = parseBlockList(getInputText(), ::parseKeyOrLock)
        locks = blocks.filter { it.second }.map { it.first }
        keys = blocks.filter { !it.second }.map { it.first }
    }


    override fun part1(): Int {
        return locks.sumOf { lock ->
            keys.count { key ->
                key.zip(lock) { a, b -> a + b }.all { it <= 7 }
            }
        }
    }


    override fun part2(): Int {
        // Merry Christmas!
        return 1
    }
}

val day25Problem = Day25Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day25Problem.testData = false
    day25Problem.runBoth(20)
}
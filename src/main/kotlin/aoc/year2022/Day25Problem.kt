package aoc.year2022

import DailyProblem
import aoc.utils.nonEmptyLines
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

class Day25Problem : DailyProblem<String>() {

    override val number = 25
    override val year = 2022
    override val name = "Full of Hot Air"
    private lateinit var data: List<List<Int>>

    private var maxInputLen: Int = 0

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { line ->
            line.reversed().map { "=-012".indexOf(it)-2 }
        }
        maxInputLen = data.maxOf { it.size }
    }

    private fun toText(num: Iterable<Int>): String {
        return num.map { "=-012"[it+2] }.dropLastWhile { it == '0' }.joinToString("").reversed()
    }

    private fun normalize(res: Array<Int>): List<Int> {
        while (res.any { it.absoluteValue > 2 }) {
            res.indices.forEach { idx ->
                val v = res[idx]
                if (v > 2) {
                    res[idx] -= 5
                    res[idx + 1] += 1
                }
                if (v < -2) {
                    res[idx] += 5
                    res[idx + 1] -= 1
                }
            }
        }
        return res.toList()
    }



    override fun part1(): String {
        val acc = Array(maxInputLen*2) { 0 }
        data.forEach { snafuNum ->
            snafuNum.forEachIndexed { index, i -> acc[index] += i }
        }
        return toText(normalize(acc))
    }


    override fun part2(): String {
        return "Merry AOC 2022!"
    }
}



val day25Problem = Day25Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day25Problem.runBoth(100)
}
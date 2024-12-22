package aoc.year2024

import DailyProblem
import aoc.utils.mutate
import aoc.utils.parseIntLines
import kotlin.time.ExperimentalTime

class Day22Problem : DailyProblem<Long>() {

    override val number = 22
    override val year = 2024
    override val name = "Monkey Market"

    private lateinit var data: List<Int>
    private lateinit var precalculated: Map<Int, Array<Long>>
    override fun commonParts() {
        data = parseIntLines(getInputText())
        precalculated = buildMap {
            data.forEach { i ->
                put(i, run(i))
            }
        }
    }


    override fun part1(): Long {
        return data.sumOf { precalculated[it]!![2000] }
    }

    private fun run(it: Int): Array<Long> {
        val a = Array(2001) { 0L }
        var acc = it.toLong()
        repeat(2001) { idx ->
            a[idx] = acc
            acc = step(acc)
        }
        return a
    }

    private fun step(it: Long): Long {
        val step1 = ((it * 64) xor it) % 16777216L
        val step2 = ((step1 / 32) xor step1) % 16777216L
        return ((step2 * 2048) xor step2) % 16777216L
    }


    private fun checkSum(a: Long, b: Long, c: Long, d: Long): Long =
        (a + 10).shl(15) + (b + 10).shl(10) + (c + 10).shl(5) + d + 10


    private fun calc2(n: Int): MutableMap<Long, Long> {
        val map: MutableMap<Long, Long> = mutableMapOf()
        val nums = precalculated[n]!!
        val digits = Array(nums.size) { nums[it] % 10 }
        val diffs = Array(digits.size - 1) { digits[it + 1] - digits[it] } // diffs[3] == digits[4]-digits[3]
        (3..1999).forEach { i ->
            val seq = checkSum(diffs[i - 3], diffs[i - 2], diffs[i - 1], diffs[i])
            if (seq !in map) map[seq] = digits[i + 1]
        }
        return map
    }

    override fun part2(): Long {
        val scoresForSequences: MutableMap<Long, Long> = HashMap<Long, Long>(160000)
        data.forEach { i ->
            calc2(i).forEach { (k, v) -> scoresForSequences.mutate(k, 0) { it + v } }
        }

        return scoresForSequences.maxOf { it.value }
    }
}

val day22Problem = Day22Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day22Problem.testData = false
    day22Problem.runBoth(10)
}
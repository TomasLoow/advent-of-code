package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.odd
import aoc.utils.sum
import java.math.BigInteger
import kotlin.time.ExperimentalTime

class Day02Problem : DailyProblem<BigInteger>() {

    override val number = 2
    override val year = 2025
    override val name = "Gift Shop"

    private lateinit var data: List<Pair<Long, Long>>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().first().split(",")
            .map { it.split("-").let { (a, b) -> Pair(a.toLong(), b.toLong()) } }
    }


    override fun part1(): BigInteger {
        return data.map { (first, last) ->
            (first..last).filter(::isSilly1).map { it.toBigInteger() }.sum()
        }.sum()
    }

    private fun isSilly1(number: Long): Boolean {
        val s = number.toString()
        if (s.length.odd) return false
        val h = s.length / 2
        return s.take(h) == s.drop(h)
    }


    private val sillyThings = listOf(
        11L to 1L..9L,
        101L to 10L..99L,
        1001L to 100L..999L,
        10001L to 1000L..9999L,
        100001L to 10000L..99999L,
        1000001L to 10000L..999999L,
        10000001L to 100000L..9999999L,
        100000001L to 1000000L..99999999L,
        1000000001L to 10000000L..999999999L,

        111L to 1L..9L,
        10101L to 10L..99L,
        1001001L to 100L..999L,
        100010001L to 1000L..9999L,
        10000100001L to 10000L..99999L,

        11111L to 1L..9L,
        101010101L to 10L..99L,

        1111111L to 1L..9L,
    )

    private fun isSilly2(number: Long): Boolean {
        sillyThings.forEach { (divisior, range) ->
            if (number % divisior == 0L && (number / divisior) in range) return true
        }
        return false
    }


    override fun part2(): BigInteger {
        return data.sumOf { (first, last) ->
            (first..last).filter {
                val silly = isSilly2(it)
                silly
            }.sumOf {
                it.toBigInteger()
            }
        }
    }
}

val day02Problem = Day02Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day02Problem.testData = false
    day02Problem.runBoth(10)
}
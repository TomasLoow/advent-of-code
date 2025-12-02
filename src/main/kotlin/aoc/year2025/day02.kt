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
        val length = s.length
        if (length.odd) return false
        val half = length / 2
        return (0..<half).all { idx -> s[idx] == s[idx + half] }
    }

    // ways do decompose numbers as p identical sequences where p is prime.
    // The largest input value is 10 digits long, so we only have to handle up to that.
    private val sillyThings = listOf(
        111L       to 1L  ..9L,     //AAA
        10101L     to 10L ..99L,    //ABABAB
        1001001L   to 100L..999L,   //ABCABCABC
        11111L     to 1L  ..9L,     //AAAAA
        101010101L to 10L ..99L,    //ABABABABAB
        1111111L   to 1L  ..9L,     //AAAAAAA
    )

    private fun isSilly2(number: Long): Boolean {
        if (isSilly1(number)) return true
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
    day02Problem.runBoth(100)
}
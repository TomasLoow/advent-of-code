package aoc.year2024

import DailyProblem
import aoc.utils.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day7Problem : DailyProblem<Long>() {

    override val number = 7
    override val year = 2024
    override val name = "Bridge Repair"

    private lateinit var data: List<Pair<Long, List<Long>>>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { line ->
            val (target, rest) = line.split(": ")
            val numbers = rest.split(" ").map { it.toLong() }
            Pair(target.toLong(), numbers)
        }
    }


    override fun part1(): Long {
        var res = 0L
        data.forEach { (target, numbers) ->
            var accs = listOf<Long>(numbers.first())
            numbers.drop(1).forEach{ x ->
                accs = buildList {
                    accs.forEach { acc ->
                        val nextByAdding = acc + x
                        val nextByMult = acc * x

                        if (nextByAdding <= target) add(nextByAdding)
                        if (nextByMult <= target) add(nextByMult)
                    }
                }
            }
            if (target in accs) res += target

        }
        return res
    }


    override fun part2(): Long {
        var res = 0L
        data.forEach { (target, numbers) ->
            var accs = listOf<Long>(numbers.first())
            numbers.drop(1).forEach{ x ->
                accs = buildList {
                    accs.forEach { acc ->
                        val nextByAdding = acc + x
                        val nextByMult = acc * x
                        val nextByConcat = acc.concat(x)

                        if (nextByAdding <= target) add(nextByAdding)
                        if (nextByMult <= target) add(nextByMult)
                        if (nextByConcat <= target) add(nextByConcat)
                    }
                }
            }
            if (target in accs) res += target

        }
        return res
    }
}

private fun Long.concat(x: Long): Long {
    if (x < 10L) return this * 10L + x
    if (x < 100L) return this * 100L + x
    if (x < 1000L) return this * 1000L + x
    if (x < 10000L) return this * 10000L + x
    if (x < 100000L) return this * 100000L + x
    if (x < 1000000L) return this * 1000000L + x
    if (x < 10000000L) return this * 10000000L + x
    if (x < 100000000L) return this * 100000000L + x
    if (x < 1000000000L) return this * 1000000000L + x
    if (x < 10000000000L) return this * 10000000000L + x
    throw NotImplementedError("Add even more branches to Long.concat or do something more clever")
}

val day7Problem = Day7Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.testData=false
    day7Problem.runBoth(100)
}
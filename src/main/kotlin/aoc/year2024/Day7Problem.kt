package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.concat
import aoc.utils.extensionFunctions.nonEmptyLines
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


val day7Problem = Day7Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.testData=true
    day7Problem.runBoth(10)
}
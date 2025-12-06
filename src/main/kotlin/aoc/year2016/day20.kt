package aoc.year2016

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.totalLengthOfCovered
import aoc.utils.parseAllPositiveInts
import aoc.utils.parseAllPositiveLongs
import aoc.utils.parseListOfPairs
import java.lang.Long.parseLong
import kotlin.math.max
import kotlin.time.ExperimentalTime

class Day20Problem : DailyProblem<Long>() {

    override val number = 20
    override val year = 2016
    override val name = "Firewall Rules"

    private lateinit var ranges: List<LongRange>

    override fun commonParts() {
        ranges = parseListOfPairs(
            getInputText(),
            ::parseLong,
            ::parseLong,
            "-"
        )
            .map { it.first..it.second }
            .sortedBy { it.first }
    }

    override fun part1(): Long {
        var candidate = 0L
        for (range in ranges) {
            if (candidate in range) {
                candidate = max(candidate, range.last + 1)
            }

        }
        return candidate
    }


    override fun part2(): Long {
        val numberOfForbidden = ranges.totalLengthOfCovered()
        val totalNum32bit = 4294967296L // 2^32
        return totalNum32bit - numberOfForbidden
    }
}

val day20Problem = Day20Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day20Problem.testData = false
    day20Problem.runBoth(1000)
}
package aoc.year2022

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

data class Rule(val source: Long, val dest: Long, val range: Long) {
    val sourceRange: LongRange
        get() {
            return source until source + range
        }

    val destRange: LongRange
        get() {
            return dest until dest + range
        }

    val offSet: Long
        get() {
            return dest - source
        }
    fun getNonOverlap(range: LongRange): List<LongRange> {
       TODO()
    }
}

typealias Step = List<Rule>


class Day5Problem() : DailyProblem<Long>() {

    private lateinit var seeds: List<Long>
    private lateinit var rules: List<Step>

    override val number = 5
    override val year = 2022
    override val name = "If You Give A Seed A Fertilizer"


    override fun commonParts() {
        val (seedsLine, restText) = this.getInputText().split("\n", limit = 2)
        seeds = seedsLine.drop(7).split(" ").map{it.toLong()}
        fun parseConvBlock(input: String): List<Rule> {
            val rules = input.nonEmptyLines().drop(1)
            return rules.map {
                val (dest,source,range) = it.split(" ")
                Rule(source = source.toLong(), dest = dest.toLong(), range = range.toLong())
            }
        }
        rules = parseBlockList(restText, ::parseConvBlock)
    }

    fun applyStepPart1(i: Long, step: Step): Long {
        for (rule in step) {
            if (i in rule.sourceRange) {
                return i + rule.offSet
            }
        }
        return i
    }
    fun applyAllStepsPart1(i: Long): Long {
        var tmp = i
        for (step in rules) {
            tmp = applyStepPart1(tmp, step)
        }
        return tmp
    }
    override fun part1(): Long {
        return seeds.map(::applyAllStepsPart1).minOf { it }.toLong()
    }

    fun applyStepPart2(inputRanges: List<LongRange>, step: Step): List<LongRange> {

        val x = inputRanges.crossMap(step) { inputRange, rule ->
            if (!inputRange.intersectRange(rule.sourceRange)) {
                Pair(listOf(inputRange), emptyList<LongRange>())
            } else {
                val umatched = inputRange.minusRange(rule.sourceRange)
                val matched = inputRange.intersectRange(rule.sourceRange)
            }
        }

        TODO()
        // return Pair(
    }

    fun applyAllStepsPart2(input: LongRange): Long {
        TODO()
    }

    override fun part2(): Long {
        val seedRanges = seeds.chunked(2).map { pair ->
            val (start, len) = pair
            (start until (start+len))
        }
        return 1L
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.runBoth(1)
}
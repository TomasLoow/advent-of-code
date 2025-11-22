package aoc.year2021

import aoc.DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.increase
import aoc.utils.extensionFunctions.iterate

private typealias PairCount = Map<String, Long>
private typealias Rules = Map<String, List<String>>


class Day14Problem : DailyProblem<Long>() {
    override val number = 14
    override val year = 2021
    override val name = "Extended Polymerization"

    private lateinit var parsedProblem: Triple<PairCount, Rules, String>

    /** Returns The count of occurrences of pairs in the input, the production rules and the sequence as is */
    private fun parsePolyFile(): Triple<PairCount, Rules, String> {
        fun parseRules(s: String): Rules {
            return parseListOfPairs(s, ::id, ::id, separator = " -> ")
                .associate { (base, gen) ->
                    Pair(base, listOf("${base[0]}$gen", "$gen${base[1]}"))
                }
        }
        val (startChain, rules) = parseTwoBlocks(getInputText(), ::id, ::parseRules)

        val startingCounts: PairCount =
            startChain
                .windowed(2)
                .groupingBy { it }
                .eachCount()
                .mapValues { entry -> entry.value.toLong() }

        return Triple(startingCounts, rules, startChain)
    }


    private fun applyRule(currentPairsCount: PairCount, rules: Rules): PairCount {
        val newCounts = currentPairsCount.toMutableMap()
        currentPairsCount.forEach { pairCount ->
            if (pairCount.key in rules) {
                newCounts.increase(pairCount.key, -pairCount.value) // Remove all such pairs...
                rules[pairCount.key]!!.forEach { newPair ->
                    newCounts.increase(newPair, pairCount.value)    // ...and add all generated new pairs.
                }
            }
        }
        return newCounts.toMap()
    }

    private fun runRules(startCounts: PairCount, rules: Rules, startChain: String, steps: Int): Long {
        val step: (PairCount) -> PairCount = { counts: PairCount -> applyRule(counts, rules) }
        val afterApplyingRules = step.iterate(startCounts, steps)

        val charCounts: MutableMap<Char, Long> = mutableMapOf(
            // Add the first and last char of the star sequence, so they don't get under-counted
            startChain.first() to 1L, startChain.last() to 1L
        )
        afterApplyingRules.forEach {
            it.key.forEach { char -> charCounts.increase(char, it.value) }
        }

        val res = (charCounts.maxOf { it.value } - charCounts.minOf { it.value })
        return res / 2
    }

    override fun commonParts() {
        this.parsedProblem = parsePolyFile()
    }
    override fun part1(): Long {
        val (startCounts, rules, startChain) = parsedProblem
        return runRules(startCounts, rules, startChain, 10)
    }

    override fun part2(): Long {
        val (startCounts, rules, startChain) = parsedProblem
        return runRules(startCounts, rules, startChain, 40)
    }
}

val day14Problem = Day14Problem()

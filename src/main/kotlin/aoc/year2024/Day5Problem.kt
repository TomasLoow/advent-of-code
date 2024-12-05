package aoc.year2024

import DailyProblem
import aoc.utils.nonEmptyLines
import aoc.utils.parseListOfPairs
import aoc.utils.parseTwoBlocks
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime

class Day5Problem : DailyProblem<Int>() {

    override val number = 5
    override val year = 2024
    override val name = "Print Queue"

    private lateinit var orderings: List<Pair<Int, Int>>
    private lateinit var updates: List<List<Int>>

    private fun parseOrdering(s: String): List<Pair<Int, Int>> {
        return parseListOfPairs(s, ::parseInt, ::parseInt, "|")
    }

    private fun parseUpdates(s: String): List<List<Int>> {
        return s.nonEmptyLines().map { line ->
            line.split(",").map { it.toInt() }
        }
    }

    override fun commonParts() {
        val parsed = parseTwoBlocks(getInputText(), ::parseOrdering, ::parseUpdates)
        orderings = parsed.first
        updates = parsed.second
    }

    fun findMiddle(list: List<Int>): Int {
        return list[list.size / 2]
    }


    private fun isOrdered(update: List<Int>): Boolean {
        return orderings.all { ord ->
            val ordered = update.indexOf(ord.first) < update.indexOf(ord.second)
            ordered || (update.indexOf(ord.first) == -1 || update.indexOf(ord.second) == -1)
        }
    }

    override fun part1(): Int {
        val orderedUpdates = updates.filter { update ->
            isOrdered(update)
        }
        return orderedUpdates.sumOf(::findMiddle)
    }

    fun fixOrdering(list: List<Int>): List<Int> {
        // No braining allowed at six in the morning. Let us just iterate until nothing changes... 😅
        val mut = list.toMutableList()
        var didStuff: Boolean
        do {
            didStuff = false
            orderings.forEach { (a, b) ->
                val idxA = mut.indexOf(a)
                val idxB = mut.indexOf(b)
                if (idxA >= 0 && idxB >= 0 && idxA > idxB) {
                    mut[idxA] = b
                    mut[idxB] = a
                    didStuff = true
                }
            }
        } while (didStuff)
        return mut
    }

    override fun part2(): Int {
        val unOrderedUpdated = updates.filter { update ->
            !isOrdered(update)
        }
        return unOrderedUpdated.map { fixOrdering(it) }.sumOf(::findMiddle)
    }
}

val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.testData = false
    day5Problem.runBoth(10)
}
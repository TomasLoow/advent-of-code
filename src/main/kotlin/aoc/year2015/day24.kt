package aoc.year2015

import aoc.DailyProblem
import aoc.utils.extensionFunctions.productL
import aoc.utils.parseIntLines
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

class Day24Problem : DailyProblem<Long>() {

    override val number = 24
    override val year = 2015
    override val name = "It Hangs in the Balance"

    private lateinit var data: List<Int>
    private var target1 by Delegates.notNull<Long>()
    private var target2 by Delegates.notNull<Long>()

    override fun commonParts() {
        data = parseIntLines(getInputText())
        target1 = data.sum() / 3L
        target2 = data.sum() / 4L
    }

    private fun subListsWithSumAndLength(data: List<Int>, sum: Int, length: Int): List<List<Int>> {
        val d = data.toIntArray()

        /** Recursive helper function */
        fun rec(index: Int, remainingLength: Int, remainingSum: Int, currentList: List<Int>): List<List<Int>> {
            if (remainingLength == 0 && remainingSum == 0) return listOf(currentList)
            if (remainingLength == 0 || remainingSum < 0 || index >= d.size) return emptyList()

            val withCurrent = if (d[index] <= remainingSum) {
                rec(index + 1, remainingLength - 1, remainingSum - d[index], currentList + d[index])
            } else emptyList()

            val withoutCurrent = rec(index + 1, remainingLength, remainingSum, currentList)

            return withCurrent + withoutCurrent
        }

        val result = rec(index = 0, remainingLength = length, remainingSum = sum, currentList = emptyList())
        return result
    }

    private fun canSplitIntoNParts(ints: List<Int>, target: Int, partCount: Int): Boolean {
        // DFS
        val stack = ArrayDeque<Pair<Int, IntArray>>()
        stack.addLast(0 to IntArray(partCount) { target })
        while (stack.isNotEmpty()) {
            val state = stack.removeFirst()
            val (idx, remainingTargets) = state
            if (remainingTargets.all { it == 0 }) return true
            if (remainingTargets.any { it < 0 }) continue
            for (i in remainingTargets.indices) {
                if (remainingTargets[i] == 0) continue
                val newArr = remainingTargets.copyOf()
                newArr[i] -= ints[idx]
                stack.addFirst(idx + 1 to newArr)
            }
        }
        return false
    }

    override fun part1(): Long {
        for (sizeOfFirst in 1..data.size) {
            val candidates = subListsWithSumAndLength(data, target1.toInt(), sizeOfFirst)
            if (candidates.isEmpty()) continue
            return candidates
                .sortedBy { it.productL() }
                .first { canSplitIntoNParts((data - it.toSet()), target1.toInt(), 2) }
                .productL()
        }
        return -1
    }

    override fun part2(): Long {
        for (sizeOfFirst in 1..data.size) {
            val candidates = subListsWithSumAndLength(data, target2.toInt(), sizeOfFirst)
            if (candidates.isEmpty()) continue
            return candidates
                .sortedBy { it.productL() }
                .first { canSplitIntoNParts((data - it.toSet()), target2.toInt(), 3) }
                .productL()
        }
        return -1
    }
}

val day24Problem = Day24Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day24Problem.testData = false
    day24Problem.runBoth(10)
}
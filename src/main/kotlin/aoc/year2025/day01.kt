package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

class Day01Problem : DailyProblem<Long>() {

    override val number = 1
    override val year = 2025
    override val name = "Problem name"

    private lateinit var turns: List<Int>

    override fun commonParts() {
        turns = getInputText().nonEmptyLines().map {
            if (it.startsWith("R")) it.drop(1).toInt() else -it.drop(1).toInt()}
    }


    override fun part1(): Long {
        val initState = Pair(50, 0)
        val endState = turns.fold(initial = initState) { (pos, zeroes), turn ->
            val new = (pos + turn + 100) % 100
            Pair(new, if (new == 0) zeroes + 1 else zeroes)
        }
        return endState.second.toLong()
    }


    override fun part2(): Long {
        val initState = Pair(50, 0)
        val endState = turns.fold(initial = initState) { (dialPos, zeroes), turn ->
            val newDialPos = (dialPos + turn)
            val passes =countZeroCrossings(dialPos, newDialPos)

            Pair((newDialPos + 100) % 100, zeroes + passes)
        }
        return endState.second.toLong()
    }

    fun countZeroCrossings(prevValue: Int, newValue: Int): Int {
        // count full rotations
        var passes = (newValue).absoluteValue / 100

        // count partial rotations that cross zero
        if ((prevValue < 0 && newValue > 0) || (prevValue > 0 && newValue < 0)) {
            passes++
        }
        // count when we end exactly on zero
        if (newValue == 0) passes++
        return passes
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.testData = false
    day01Problem.runBoth(1000)
}
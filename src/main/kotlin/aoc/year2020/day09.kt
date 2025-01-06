package aoc.year2020

import DailyProblem
import aoc.utils.extensionFunctions.allUnorderedPairs
import aoc.utils.extensionFunctions.minAndMaxL
import aoc.utils.parseLongLines
import kotlin.time.ExperimentalTime


class Day09Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2020
    override val name = "Encoding Error"

    private lateinit var input: List<Long>
    private var pt1solutiuon: Long? = null
    private var preambleSize = 25

    override fun commonParts() {
        input = parseLongLines(getInputText())
        if (testData) {
            preambleSize = 5
        }
    }


    override fun part1(): Long {
        val res = input.windowed(preambleSize + 1).first { it ->
            val target = it.last()
            val preceeding = it.take(preambleSize)
            !preceeding.allUnorderedPairs().any { (a, b) -> a + b == target }
        }.last()
        pt1solutiuon = res
        return res
    }


    override fun part2(): Long {
        if (pt1solutiuon == null) {
            pt1solutiuon = part1()
        }
        var a = 0
        var b = 1
        var currentSum = input.subList(a, b + 1).sum()
        while (currentSum != pt1solutiuon!!) {
            if (currentSum < pt1solutiuon!!) b++ else a++
            currentSum = input.subList(a, b + 1).sum()
        }
        return input.subList(a, b + 1).minAndMaxL().let { (min, max) -> min + max }
    }
}

val day09Problem = Day09Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day09Problem.testData = false
    day09Problem.runBoth(10)
}
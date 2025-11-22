package aoc.year2019

import aoc.DailyProblem
import aoc.utils.extensionFunctions.permutationsSequence
import aoc.utils.parseIntCodeProgram
import kotlin.time.ExperimentalTime

class Day07Problem : DailyProblem<Long>() {

    override val number = 7
    override val year = 2019
    override val name = "Amplification Circuit"

    private lateinit var initMemory: Array<Long>

    override fun commonParts() {
        initMemory = parseIntCodeProgram(getInputText())
    }

    fun chain(computers: List<IntCode>, input: Long): RunResult {
        var i:Long? = input
        var lastResult: RunResult? = null
        computers.forEach { computer ->
            computer.writeInput(i!!)
            val r = computer.runUntilNeedsInputOrHalt()
            if (r.output.size != 1) throw IllegalStateException("Too much output")
            i = r.output.first()
            lastResult = r
        }
        return lastResult!!
    }

    fun runWithoutFeedback(computers: List<IntCode>) =
        listOf(0L, 1L, 2L, 3L, 4L).permutationsSequence().maxOf { perm ->
            computers.forEach { it.reset() }
            computers.zip(perm).forEach { (comp, value) -> comp.writeInput(listOf(value)) }
            chain(computers, 0).output.first()
        }

    fun runWithFeedback(computers: List<IntCode>) =
        listOf(5L, 6L, 7L, 8L, 9L).permutationsSequence().maxOf { perm ->
            computers.forEach { it.reset() }
            computers.zip(perm).forEach { (comp, value) -> comp.writeInput(listOf(value)) }

            var i = 0L
            var finalResult: Long
            while (true) {
                val res = chain(computers, i)
                finalResult = res.output.first()
                i = res.output.first()
                if (res.halted) {
                    break
                }
            }
            finalResult
        }

    override fun part1(): Long {
        val computers = (0..4).map { i -> IntCode(initMemory, "comp$i") }
        return runWithoutFeedback(computers)
    }

    override fun part2(): Long {
        val computers = (0..4).map { i -> IntCode(initMemory, "comp$i") }
        return runWithFeedback(computers)
    }
}

@Suppress("unused")
fun Sequence<Int>.wirePeek(name: String): Sequence<Int> {
    return this // this.map { println("$name>$it"); it }
}

val day07Problem = Day07Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day07Problem.testData = false
    day07Problem.runBoth(1)
}
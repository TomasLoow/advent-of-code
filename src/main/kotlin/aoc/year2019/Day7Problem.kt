package aoc.year2019

import DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.permutationsSequence
import kotlin.time.ExperimentalTime

class Day7Problem : DailyProblem<Int>() {

    override val number = 7
    override val year = 2019
    override val name = "Amplification Circuit"

    private lateinit var initMemory: Array<Int>

    override fun commonParts() {
        // initMemory = arrayOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,
        //     101,5,23,23,1,24,23,23,4,23,99,0,0)
        initMemory = parseOneLineOfSeparated(
            getInputText().replace("\n", "").nonEmptyLines().first(),
            String::toInt,
            ","
        ).toTypedArray()
    }

    fun chain(computers: List<IntCode>, input: Int): RunResult {
        var i:Int? = input
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
        listOf(0, 1, 2, 3, 4).permutationsSequence().maxOf { perm ->
            computers.forEach { it.reset() }
            computers.zip(perm).forEach { (comp, value) -> comp.writeInput(listOf(value)) }
            chain(computers, 0).output!!.first()
        }

    fun runWithFeedback(computers: List<IntCode>) =
        listOf(5, 6, 7, 8, 9).permutationsSequence().maxOf { perm ->
            computers.forEach { it.reset() }
            computers.zip(perm).forEach { (comp, value) -> comp.writeInput(listOf(value)) }

            var i = 0
            var finalResult = 0
            while (true) {
                val res = chain(computers, i)
                if (res.output != null) {
                    finalResult = res.output.first()
                    i = res.output.first()
                }
                if (res.halted) {
                    break
                }
            }
            finalResult
        }

    override fun part1(): Int {
        val computers = (0..4).map { i -> IntCode(initMemory, "comp$i") }
        return runWithoutFeedback(computers)
    }

    override fun part2(): Int {
        val computers = (0..4).map { i -> IntCode(initMemory, "comp$i") }
        return runWithFeedback(computers)
    }
}

fun Sequence<Int>.wirePeek(name: String): Sequence<Int> {
    return this // this.map { println("$name>$it"); it }
}

val day7Problem = Day7Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.testData = false
    day7Problem.runBoth(1)
}
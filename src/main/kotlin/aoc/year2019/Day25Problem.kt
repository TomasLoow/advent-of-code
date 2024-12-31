package aoc.year2019

import DailyProblem
import aoc.utils.parseIntCodeComputer
import kotlin.time.ExperimentalTime

val badObjects = listOf(
    "giant electromagnet",
    "infinite loop",
    "photons",
    "molten lava",
    "shell",
    "escape pod"
)

class Day25Problem : DailyProblem<Int>() {

    override val number = 25
    override val year = 2019
    override val name = "Cryostasis"

    private lateinit var c: IntCode

    override fun commonParts() {
        c = parseIntCodeComputer(getInputText())
    }


    private fun runTillInput() {
        val o = c.runUntilNeedsInputOrHalt()
        o.output.map { it.toInt().toChar() }.joinToString("").also { print(it) }
    }

    private val shortCuts = mapOf(
        "s" to "south",
        "w" to "west",
        "n" to "north",
        "e" to "east",
        "t" to "take",
        "d" to "drop",
        "i" to "inv"
    )

    private fun userInput() {
        var stringInput = readLine()!!
        shortCuts.forEach { (k, v) ->
            if (stringInput.startsWith(k)) {
                stringInput = stringInput.replaceFirst(k, v)
            }
        }

        stringInput.map { c.writeInput(it.code.toLong()) }
        c.writeInput(10) // newline
    }

    private fun manualGame() {
        while (true) {
            runTillInput()
            userInput()
        }
    }

    override fun part1(): Int {
        manualGame()
        return 1
    }

    override fun part2(): Int {
        // Merry Christmas!
        return 1
    }
}

val day25Problem = Day25Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day25Problem.testData = false
    day25Problem.runBoth(1)
}
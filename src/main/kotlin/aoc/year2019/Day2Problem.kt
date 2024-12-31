package aoc.year2019

import DailyProblem
import aoc.utils.parseIntCodeProgram
import kotlin.time.ExperimentalTime

class Day2Problem : DailyProblem<Long>() {

    override val number = 2
    override val year = 2019
    override val name = "Program Alarm"

    private lateinit var initMemory: Array<Long>

    override fun commonParts() {
        initMemory = parseIntCodeProgram(getInputText())
    }

    override fun part1(): Long {
        val memory = initMemory.copyOf()
        memory[1] = 12
        memory[2] = 2
        val computer = IntCode(memory)

        computer.runUntilHalt()

        println(computer.memory)
        return computer.memory[0]
    }

    override fun part2(): Long {
        for (verb in 0L..99L) {
            for (noun in 0L..99L) {
                val memory = initMemory.copyOf()
                memory[1] = noun
                memory[2] = verb
                val computer = IntCode(memory)
                computer.runUntilHalt()
                if (computer.memory[0] == 19690720L) return 100 * noun + verb
            }
        }
        return 1
    }
}

val day2Problem = Day2Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day2Problem.testData = false
    day2Problem.runBoth(1)
}
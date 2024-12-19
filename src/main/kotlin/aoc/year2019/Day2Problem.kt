package aoc.year2019

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day2Problem : DailyProblem<Int>() {

    override val number = 2
    override val year = 2019
    override val name = "Program Alarm"

    private lateinit var initMemory: Array<Int>

    override fun commonParts() {
        initMemory = parseOneLineOfSeparated(getInputText().nonEmptyLines().first(), String::toInt, ",").toTypedArray()
    }

    override fun part1(): Int {
        val memory = initMemory.copyOf()
        memory[1] = 12
        memory[2] = 2
        val computer = IntCode(memory)

        computer.run()

        println(computer.memory)
        return computer.memory[0]
    }

    override fun part2(): Int {
        for (verb in 0..99) {
            for (noun in 0..99) {
                try {
                    val memory = initMemory.copyOf()
                    memory[1] = noun
                    memory[2] = verb
                    val computer = IntCode(memory)
                    computer.run()
                    if (computer.memory[0] == 19690720) return 100 * noun + verb
                } catch (e: ExecutionFailed) {
                    continue
                }
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
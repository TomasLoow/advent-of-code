package aoc.year2020

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

private sealed interface Instruction {
    data class Acc(val value: Int) : Instruction
    data class Jmp(val value: Int) : Instruction
    data class Nop(val value: Int) : Instruction
}

class Day08Problem : DailyProblem<Long>() {

    override val number = 8
    override val year = 2020
    override val name = "Handheld Halting"

    private lateinit var program: Array<Instruction>

    override fun commonParts() {
        program = getInputText().nonEmptyLines().map {
            val (op, value) = it.split(" ")
            when (op) {
                "nop" -> Instruction.Nop(value.toInt())
                "jmp" -> Instruction.Jmp(value.toInt())
                "acc" -> Instruction.Acc(value.toInt())
                else -> throw Exception("Bad Input")
            }
        }.toTypedArray()
    }

    private data class RunResult(val halted: Boolean, val accumulator: Int, val executed: BooleanArray)

    private fun run(instructions: Array<Instruction>): RunResult {
        val hasRun = BooleanArray(instructions.size)
        var pointer = 0
        var accumulator = 0
        while (pointer < instructions.size) {
            if (hasRun[pointer]) return RunResult(false, accumulator, hasRun)
            hasRun[pointer] = true
            when (val instruction = instructions[pointer]) {
                is Instruction.Acc -> {
                    accumulator += instruction.value
                    pointer++
                }
                is Instruction.Jmp -> {
                    pointer += instruction.value
                }
                is Instruction.Nop -> {
                    pointer++
                }
            }
        }
        return RunResult(true, accumulator, hasRun)
    }

    override fun part1(): Long {
        return run(instructions= program).accumulator.toLong()
    }


    override fun part2(): Long {
        val (_, _, hasRun) = run(instructions= program)

        for (i in program.indices) {
            if (!hasRun[i] || program[i] is Instruction.Acc) continue
            val op = program[i]
            val newProgram = program.copyOf()
            newProgram[i] = when(op) {
                is Instruction.Nop -> Instruction.Jmp(op.value)
                is Instruction.Jmp -> Instruction.Nop(op.value)
                else -> throw Exception("That should be impossible")
            }
            val (success, acc, _) = run(newProgram)
            if (success) return acc.toLong()

        }
        return 1
    }
}

val day08Problem = Day08Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day08Problem.testData = false
    day08Problem.runBoth(100)
}
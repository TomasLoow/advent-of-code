package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.even
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

private typealias Register = String

private sealed interface Instruction {
    data class HLF(val reg: Register): Instruction
    data class TPL(val reg: Register): Instruction
    data class INC(val reg: Register): Instruction
    data class JMP(val offset: Int): Instruction
    data class JIE(val reg: Register, val offset: Int): Instruction
    data class JIO(val reg: Register, val offset: Int): Instruction
}

private data class  ExecutionState(var instructionPointer: Int=0, val registers : MutableMap<Register, Int> = mutableMapOf("a" to 0, "b" to 0))
private class ExecutionComplete : Throwable()

class Day23Problem : DailyProblem<Int>() {

    override val number = 23
    override val year = 2015
    override val name = "Opening the Turing Lock"

    private lateinit var program: List<Instruction>


    private fun parseInstruction(s: String): Instruction {
        val split = s.split(Regex(",? "))
        val instruction = when (split.component1()) {
            "hlf" -> Instruction.HLF(split.component2())
            "tpl" -> Instruction.TPL(split.component2())
            "inc" -> Instruction.INC(split.component2())
            "jmp" -> Instruction.JMP(split.component2().toInt())
            "jie" -> Instruction.JIE(split.component2(), split.component3().toInt())
            "jio" -> Instruction.JIO(split.component2(), split.component3().toInt())
            else -> throw Exception("Invalid input")
        }
        return instruction
    }

    override fun commonParts() {
        program = getInputText().nonEmptyLines().map(::parseInstruction)
    }

    private fun step(program: List<Instruction>, state:ExecutionState) {
        val i = program[state.instructionPointer]
        var offset = 1
        when(i) {
            is Instruction.HLF -> {
                state.registers[i.reg] = state.registers[i.reg]!! / 2
            }
            is Instruction.INC-> {
                state.registers[i.reg] = state.registers[i.reg]!! + 1
            }
            is Instruction.TPL-> {
                state.registers[i.reg] = state.registers[i.reg]!! * 3
            }
            is Instruction.JMP -> { offset = i.offset }
            is Instruction.JIE -> {
                val current = state.registers[i.reg]!!
                offset = if (current.even) i.offset else 1
            }
            is Instruction.JIO-> {
                val current = state.registers[i.reg]!!
                offset = if (current == 1) i.offset else 1
            }

        }
        state.instructionPointer += offset
        if (state.instructionPointer !in program.indices) throw ExecutionComplete()
    }

    private fun runProgramUntilHalt(state: ExecutionState) {
        try {
            while (true) {
                step(program, state)
            }
        } catch (e: ExecutionComplete) {
            //done
        }
    }

    override fun part1(): Int {
        val state = ExecutionState()
        runProgramUntilHalt(state)
        return state.registers["b"]!!
    }

    override fun part2(): Int {
        val state = ExecutionState(registers = mutableMapOf("a" to 1, "b" to 0))
        runProgramUntilHalt(state)
        return state.registers["b"]!!
    }
}

val day23Problem = Day23Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day23Problem.testData=false
    day23Problem.runBoth(10)
}
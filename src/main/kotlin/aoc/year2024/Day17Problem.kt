package aoc.year2024

import DailyProblem
import aoc.utils.parseOneLineOfSeparated
import aoc.utils.parseTwoBlocks
import kotlin.math.pow
import kotlin.math.truncate
import kotlin.time.ExperimentalTime

data class Register(var a: Long = 0L, var b: Long = 0L, var c: Long = 0L)
data class State(
    val registers: Register,
    val ops: Array<Int>,
    var pointer: Int
)


class Day17Problem : DailyProblem<String>() {

    override val number = 17
    override val year = 2024
    override val name = "Chronospatial Computer"

    private lateinit var data: Pair<Register, Array<Int>>

    override fun commonParts() {
        fun parseRegisters(input: String): Register {
            val findAllNumbers = """(\d+)""".toRegex()
            val (a, b, c) = findAllNumbers.findAll(input).map { it.value.toLong() }.toList()
            return Register(a, b, c)
        }

        fun parseOps(input: String): Array<Int> {
            return parseOneLineOfSeparated(input.removePrefix("Program: "), { it.toInt() }, ",").toTypedArray()
        }
        data = parseTwoBlocks(getInputText(), ::parseRegisters, ::parseOps)
    }


    override fun part1(): String {
        val state = State(data.first, data.second, 0)
        val r = runUntilHalt(state)
        println(r)
        return r.joinToString(",")
    }

    fun runUntilHalt(state: State): List<Long> {
        val res = mutableListOf<Long>()
        while (state.pointer < state.ops.indices.last) {
            val op = state.ops[state.pointer]
            val operand = state.ops[state.pointer + 1]
            println(state.registers)
            when (op) {
                0 -> state.registers.a = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                1 -> state.registers.b = state.registers.b xor operand.toLong()
                2 -> state.registers.b = combo(state, operand) % 8
                3 -> if (state.registers.a != 0L) {
                    state.pointer = operand
                    continue
                }

                4 -> state.registers.b = state.registers.b xor state.registers.c
                5 -> res.add(combo(state, operand) % 8)
                6 -> state.registers.b = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                7 -> state.registers.c = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                else -> TODO("Invalid opcode")
            }
            state.pointer += 2
        }
        return res.toList()
    }

    private fun combo(state: State, operand: Int): Long {
        if (operand in (0..3)) return operand.toLong()
        if (operand == 4) return state.registers.a
        if (operand == 5) return state.registers.b
        if (operand == 6) return state.registers.c
        TODO()
    }


    override fun part2(): String {
        return "1"
    }
}

val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    // 3,7,1,6,3,1,0,5,0
    day17Problem.testData = false
    day17Problem.runBoth(1)
}
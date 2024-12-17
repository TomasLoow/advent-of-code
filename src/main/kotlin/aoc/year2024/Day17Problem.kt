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
    val ops: List<Long>,
    var pointer: Long
)

class Day17Problem : DailyProblem<String>() {

    override val number = 17
    override val year = 2024
    override val name = "Chronospatial Computer"

    private lateinit var initialReg: Register
    lateinit var program: List<Long>

    override fun commonParts() {
        fun parseRegisters(input: String): Register {
            val findAllNumbers = """(\d+)""".toRegex()
            val (a, b, c) = findAllNumbers.findAll(input).map { it.value.toLong() }.toList()
            return Register(a, b, c)
        }

        fun parseOps(input: String): List<Long> {
            return parseOneLineOfSeparated(input.removePrefix("Program: "), { it.toLong() }, ",")
        }

        val data = parseTwoBlocks(getInputText(), ::parseRegisters, ::parseOps)
        initialReg = data.first
        program = data.second
    }


    override fun part1(): String {
        val state = State(initialReg, program, 0)
        val r = runAsSequence(state).toList()
        return r.joinToString(",")
    }

    fun runAsSequence(state: State): Sequence<Long> {
        fun combo(state: State, operand: Long): Long {
            if (operand in (0..3)) return operand.toLong()
            if (operand == 4L) return state.registers.a
            if (operand == 5L) return state.registers.b
            if (operand == 6L) return state.registers.c
            TODO()
        }
        return sequence {
            while (state.pointer < state.ops.indices.last) {
                val op = state.ops[state.pointer.toInt()]
                val operand = state.ops[state.pointer.toInt() + 1]
                when (op) {
                    0L-> state.registers.a = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                    1L-> state.registers.b = state.registers.b xor operand.toLong()
                    2L-> state.registers.b = combo(state, operand) % 8
                    3L-> if (state.registers.a != 0L) {
                        state.pointer = operand
                        continue
                    }

                    4L -> state.registers.b = state.registers.b xor state.registers.c
                    5L -> yield(combo(state, operand) % 8)
                    6L -> state.registers.b = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                    7L -> state.registers.c = truncate(state.registers.a / 2.0.pow(1.0 * combo(state, operand))).toLong()
                    else -> TODO("Invalid opcode")
                }
                state.pointer += 2
            }
        }
    }

    /** Runs a program with a set to the value you get by combining l as a list of 3-digit numbers */
    fun runList(l: List<Int>): List<Long> {
        return runForA(tribitListToLong(l))
    }

    fun runForA(a: Long): List<Long> {
        return runAsSequence(State(Register(a = a), program, 0)).toList()
    }

    override fun part2(): String {
        /*
        Based on inspecting the input files, Only the last 9 bit affect the yielded value
        So we can Construct the quine by starting with three digits that generate the first int of the program
        and then try to add digits in front of them so the generated output matches more and more digits.
        */

        val allTriples = (0..511).map {
            Triple(it.shr(6), (it.shr(3)) % 8, it % 8)
        }
        var candidatePaths: Set<List<Int>> = allTriples.map { it.toList() }.toSet()
        candidatePaths = candidatePaths.filter { runList(it).first() == program.first().toLong() }.toSet()
        var correctOutput = 0
        while (correctOutput < program.size) {
            correctOutput++
            val extendedPaths = candidatePaths.toList().flatMap { p ->
                (0..7).flatMap { d ->
                    val candidate = listOf(d) + p
                    val out = runList(candidate)
                    if (out.zip(program)
                            .takeWhile { (a, b) -> a == b.toLong() }.size >= correctOutput
                    ) listOf(candidate)
                    else emptyList()
                }
            }.toSet()
            if (extendedPaths.isEmpty()) break
            else candidatePaths = extendedPaths
        }
        val res = candidatePaths
            .map { it to runList(it) }
            .filter { it.second.size == program.size }
            .filter { it.second.zip(program).all { (a,b) -> a==b } }
            .sortedBy{ it.first.toString()}
            .first()

        return tribitListToLong(res.first).toString()
    }

    /**
     * Converts a list of integers, where each integer represents a tribit (a single base-8 digit),
     * into a single long integer. The tribits in the list are treated sequentially to construct
     * the resulting number in base-8.
     *
     * @param listOf A list of integers representing tribits (valid integers from 0 to 7).
     * @return A single long integer created by interpreting the input list as a base-8 number.
     */
    private fun tribitListToLong(listOf: List<Int>): Long {
        var res = 0L
        listOf.forEach { res = 8L * res + it }
        return res
    }
}


val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.testData = false
    day17Problem.runBoth(100)
}
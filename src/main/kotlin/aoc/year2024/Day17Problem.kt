package aoc.year2024

import DailyProblem
import aoc.utils.emptyMutableSet
import aoc.utils.parseOneLineOfSeparated
import aoc.utils.parseTwoBlocks
import kotlin.math.pow
import kotlin.math.truncate
import kotlin.time.ExperimentalTime

data class Register(var a: Long = 0L, var b: Long = 0L, var c: Long = 0L)
data class State(
    val registers: Register,
    val ops: ThreeBitWordList,
    var pointer: Int
)

typealias ThreeBitWordList = List<Int>

class Day17Problem : DailyProblem<String>() {

    override val number = 17
    override val year = 2024
    override val name = "Chronospatial Computer"

    private lateinit var initialReg: Register
    lateinit var program: ThreeBitWordList

    override fun commonParts() {
        fun parseRegisters(input: String): Register {
            val findAllNumbers = """(\d+)""".toRegex()
            val (a, b, c) = findAllNumbers.findAll(input).map { it.value.toLong() }.toList()
            return Register(a, b, c)
        }

        fun parseOps(input: String): ThreeBitWordList {
            return parseOneLineOfSeparated(input.removePrefix("Program: "), { it.toInt() }, ",")
        }

        val data = parseTwoBlocks(getInputText(), ::parseRegisters, ::parseOps)
        initialReg = data.first
        program = data.second
    }


    override fun part1(): String {
        val state = State(initialReg, program, 0)
        val r = runForA(state.registers.a)
        return r.joinToString(",")
    }

    fun run(state: State): List<Int> {
        fun combo(state: State, operand: Int): Long {
            if (operand in (0..3)) return operand.toLong()
            if (operand == 4) return state.registers.a
            if (operand == 5) return state.registers.b
            if (operand == 6) return state.registers.c
            TODO()
        }
        return buildList {
            while (state.pointer < state.ops.indices.last) {
                val op = state.ops[state.pointer]
                val operand = state.ops[state.pointer + 1]
                when (op) {
                    0 -> state.registers.a = state.registers.a.shr(combo(state, operand).toInt())
                    1 -> state.registers.b = state.registers.b xor operand.toLong()
                    2 -> state.registers.b = combo(state, operand) and 7
                    3 -> if (state.registers.a != 0L) {
                        state.pointer = operand
                        continue
                    }
                    4 -> state.registers.b = state.registers.b xor state.registers.c
                    5 -> add((combo(state, operand) and 7).toInt())
                    6 -> state.registers.b = state.registers.a.shr(combo(state, operand).toInt())
                    7 -> state.registers.c = state.registers.a.shr(combo(state, operand).toInt())
                    else -> TODO("Invalid opcode")
                }
                state.pointer += 2
            }
        }
    }

    /** Runs a program with a set to the value you get by combining l as a list of 3-digit numbers */
    private fun runThreeBitWordList(l: ThreeBitWordList): ThreeBitWordList {
        return runForA(triBitWordListToLong(l))
    }

    fun runForA(a: Long): ThreeBitWordList {
        return run(State(Register(a = a), program, 0))
    }

    override fun part2(): String {
        /*
        Based on inspecting the input files, Only the last 9 bit affect the yielded value
        So we can Construct the quine by starting with three digits that generate the first int of the program
        and then try to add digits in front of them so the generated output matches more and more digits.
        */

        val allTriples = (0..511).map {
            listOf(it.shr(6), (it.shr(3)) and 7, it and 7)
        }
        val candidatePathsStack: MutableList<ThreeBitWordList> = allTriples.map { it }.toMutableList()
        candidatePathsStack.removeIf { runThreeBitWordList(it).first() != program.first() }
        val sols = emptyMutableSet<ThreeBitWordList>()
        while (candidatePathsStack.isNotEmpty()) {
            val candidate = candidatePathsStack.removeFirst()
            if (candidate.size >= program.size ) continue
            val expectedCorrectOutputLen = candidate.size - 2
            for (d in (0..7)) {
                val newCandidate = buildList { add(d); addAll(candidate) }
                val out = runThreeBitWordList(newCandidate)
                if (out.take(expectedCorrectOutputLen) == program.take(expectedCorrectOutputLen)) {
                    candidatePathsStack.add(newCandidate)
                    if (out == program) {
                        sols.add(newCandidate)
                        break
                    }
                }
            }
        }
        return sols.map { triBitWordListToLong(it) }.minOf { it }.toString()
    }

    /**
     * Converts a list of integers, where each integer represents a ThreeBitWord (a single base-8 digit),
     * into a single long integer. The ThreeBitWords in the list are treated sequentially to construct
     * the resulting number in base-8.
     * triBitWordListToLong(listOf(2,5,1)) == /* 010 101 001 == 0 10101001 */ ==  169
     *
     *  @param listOf A list of integers representing ThreeBitWord (valid integers from 0 to 7).
     * @return A single long integer created by interpreting the input list as a base-8 number.
     */
    private fun triBitWordListToLong(listOf: ThreeBitWordList): Long =
        listOf.fold(0L) { acc: Long, i: Int ->  8L * acc + i }
}


val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.testData = false
    day17Problem.runBoth(100)
}
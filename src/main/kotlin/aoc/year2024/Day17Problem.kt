package aoc.year2024

import DailyProblem
import aoc.utils.parseOneLineOfSeparated
import aoc.utils.parseTwoBlocks
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
        So we can Construct the quine by starting with a digit that generate the last int of the program
        and then in a DFS try to add digits after them so the generated output matches more and more
        digits of the suffix of the program.
        */
        val stack: MutableList<ThreeBitWordList> = (0..7).filter { runForA(it.toLong()).first() == program.last() }
            .map { listOf(it) }
            .toMutableList()

        var sol: ThreeBitWordList? = null
        while (stack.isNotEmpty()) {
            val candidate = stack.removeFirst()
            val size = candidate.size
            val new = mutableListOf<ThreeBitWordList>()
            for (d in (0..7)) {
                val newCandidate = candidate + d
                val out = runThreeBitWordList(newCandidate)
                if (out.takeLast(size) == program.takeLast(size)) {
                    new.add(newCandidate)
                    if (out == program) {
                        sol = newCandidate
                        break
                    }
                }
            }
            new.reversed().forEach { stack.add(it) }  // reverse to ensure we visit numbers in ascending order.
        }
        return triBitWordListToLong(sol!!).toString()
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
        listOf.fold(0L) { acc: Long, i: Int -> 8L * acc + i }
}


val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.testData = false
    day17Problem.runBoth(100)
}
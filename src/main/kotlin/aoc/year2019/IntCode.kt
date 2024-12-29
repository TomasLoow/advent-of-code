package aoc.year2019

import aoc.utils.emptyMutableList

class ExecutionFailed(val reason: String = "") : Throwable()

const val OPCODE_ADD = 1
const val OPCODE_MUL = 2
const val OPCODE_HALT = 99
const val OPCODE_INPUT = 3
const val OPCODE_OUTPUT = 4
const val OPCODE_JUMP_IF_TRUE = 5
const val OPCODE_JUMP_IF_FALSE = 6
const val OPCODE_LESS_THAN = 7
const val OPCODE_EQUALS = 8

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1


data class StepResult(val halted: Boolean, val output: Int? = null, val readInput : Boolean = false, val needsInput: Boolean = false)
data class RunResult(val output: List<Int>, val halted: Boolean)

class IntCode(var startingMemory: Array<Int>, val name: String = "IntCode") {
    var memory: Array<Int>

    init {
        memory = Array(startingMemory.size) { if (it in startingMemory.indices) startingMemory.elementAt(it) else 0 }
    }
    var ptr = 0
    var input : MutableList<Int> = emptyMutableList()

    fun input(i: Int) {
        input.add(i)
    }

    fun input(i: Collection<Int>) {
        i.forEach { input(it) }
    }

    fun reset() {
        ptr = 0
        input.clear()
        memory = Array(startingMemory.size*2) { if (it in startingMemory.indices) startingMemory.elementAt(it) else 0 }
    }

    private fun op(paramModes: Int, action: (Int, Int) -> Int) {
        val (pm3, pm2, pm1) = paramModes.toString().padStart(3, '0').map { it.toString().toInt() }
        if (ptr + 3 >= memory.size) throw ExecutionFailed("Out of bounds")
        if (pm3 == 1) throw ExecutionFailed("Bad param mode for operand 3")
        val operand1 = memory[ptr + 1]
        val operand2 = memory[ptr + 2]
        val target = memory[ptr + 3]
        val a = if (pm1 == PARAM_MODE_POSITION) memory[operand1] else operand1
        val b = if (pm2 == PARAM_MODE_POSITION) memory[operand2] else operand2
        memory[target] = action(a,b)
        ptr += 4
    }

    private fun opAdd(paramModes: Int) = op(paramModes) { a, b -> a + b }
    private fun opMul(paramModes: Int) = op(paramModes) { a, b -> a * b }
    private fun opEquals(paramModes: Int) = op(paramModes) { a, b -> if (a == b) 1 else 0 }
    private fun opLessThan(paramModes: Int) = op(paramModes) { a, b -> if (a < b) 1 else 0 }

    private fun opInput() {
        val target = memory[ptr + 1]
        if (target >= memory.size) throw ExecutionFailed("Out of bounds")
        val i = input.removeFirst()
        memory[target] = i
        ptr += 2
    }

    private fun opOutput(paramModes: Int): Int {
        if (paramModes == PARAM_MODE_IMMEDIATE) {
            val out = memory[ptr + 1]
            ptr += 2
            return out
        }
        val target = memory[ptr + 1]
        if (target >= memory.size) throw ExecutionFailed("Out of bounds")
        val output = memory[target]
        ptr += 2
        return output
    }

    private fun opJump(paramModes: Int, target: Boolean) {
        val (pm2, pm1) = paramModes.toString().padStart(2, '0').map { it.toString().toInt() }
        val operand1 = memory[ptr + 1]
        val o1value = if (pm1 == PARAM_MODE_POSITION) memory[operand1] else operand1

        if ((o1value != 0) == target) {
            val operand2 = memory[ptr + 2]
            val o2value = if (pm2 == PARAM_MODE_POSITION) memory[operand2] else operand2
            ptr = o2value
        } else {
            ptr += 3
        }
    }

    fun step(): StepResult {
        val opcode = memory[ptr]
        val op = opcode % 100
        val paramModes = opcode / 100

        var output: Int? = null
        var input = false
        when (op) {
            OPCODE_HALT -> return StepResult(halted = true)
            OPCODE_ADD -> opAdd(paramModes)
            OPCODE_MUL -> opMul(paramModes)
            OPCODE_INPUT -> {
                if (this.input.isEmpty()) { return StepResult(needsInput = true, halted = false)}
                opInput();
                input = true }
            OPCODE_OUTPUT -> { output = opOutput(paramModes) }
            OPCODE_EQUALS -> opEquals(paramModes)
            OPCODE_LESS_THAN -> opLessThan(paramModes)
            OPCODE_JUMP_IF_TRUE -> opJump(paramModes, true)
            OPCODE_JUMP_IF_FALSE -> opJump(paramModes, false)
            else -> throw Exception("Unknown instruction: $op")
        }
        return StepResult(halted = false, output = output, readInput = input)
    }

    fun runStreaming(input: Collection<Int> = emptyList()): Sequence<Int> {
        input(input)
        return sequence {
            while (true) {
                val res = step()
                if (res.output != null) {
                    yield(res.output)
                }
                if (res.halted) break
            }
        }
    }

    fun runFully(input: Collection<Int> = emptyList()): List<Int> {
        return runStreaming(input).toList()
    }

    fun runUntilOutput(): RunResult {
        while (true) {
            val res = step()
            if (res.output != null) {
                return RunResult(output = listOf(res.output), halted = res.halted)
            }
            if (res.halted) return RunResult(output = emptyList(), halted = res.halted)
        }
    }

    fun runUntilNeedsInputOrHalt(): RunResult {
        var output = emptyList<Int>()
        while (true) {
            val res = step()
            if (res.output != null) {
                output += res.output
            }
            if (res.needsInput || res.halted) return RunResult(output = output, halted = res.halted)
        }
    }
}
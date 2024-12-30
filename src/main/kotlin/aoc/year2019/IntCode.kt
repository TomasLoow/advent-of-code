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

private data class StepResult(val halted: Boolean, val output: Int? = null, val readInput : Boolean = false, val needsInput: Boolean = false)
data class RunResult(val output: List<Int>, val halted: Boolean)

class IntCode(var startingMemory: Array<Int>, val name: String = "IntCode") {
    var memory: Array<Int>

    init {
        memory = Array(startingMemory.size) { if (it in startingMemory.indices) startingMemory.elementAt(it) else 0 }
    }
    var instructionPointer = 0
    var inputBuffer : MutableList<Int> = emptyMutableList()

    fun writeInput(i: Int) {
        inputBuffer.add(i)
    }

    fun writeInput(i: Collection<Int>) {
        i.forEach { writeInput(it) }
    }

    fun reset() {
        instructionPointer = 0
        inputBuffer.clear()
        memory = Array(startingMemory.size) { if (it in startingMemory.indices) startingMemory.elementAt(it) else 0 }
    }

    private fun getValue(i: Int, paramMode: Int): Int {
        return when (paramMode) {
            PARAM_MODE_POSITION -> memory[i]
            PARAM_MODE_IMMEDIATE -> i
            else -> throw ExecutionFailed("Unknown param mode: $paramMode")
        }
    }

    private fun hasInput() = this.inputBuffer.isNotEmpty()

    private fun binaryOp(paramModes: Int, action: (Int, Int) -> Int) {
        val (pm3, pm2, pm1) = paramModes.toString().padStart(3, '0').map { it.toString().toInt() }
        if (pm3 == 1) throw ExecutionFailed("Bad param mode for operand 3")
        val a = getValue (memory[instructionPointer + 1], pm1)
        val b = getValue (memory[instructionPointer + 2], pm2)
        val target = memory[instructionPointer + 3]
        memory[target] = action(a,b)
        instructionPointer += 4
    }

    private fun opAdd(paramModes: Int) = binaryOp(paramModes) { a, b -> a + b }
    private fun opMul(paramModes: Int) = binaryOp(paramModes) { a, b -> a * b }
    private fun opEquals(paramModes: Int) = binaryOp(paramModes) { a, b -> if (a == b) 1 else 0 }
    private fun opLessThan(paramModes: Int) = binaryOp(paramModes) { a, b -> if (a < b) 1 else 0 }

    private fun opInput() {
        val target = memory[instructionPointer + 1]
        if (target >= memory.size) throw ExecutionFailed("Out of bounds")
        val i = inputBuffer.removeFirst()
        memory[target] = i
        instructionPointer += 2
    }

    private fun opOutput(paramModes: Int): Int {
        if (paramModes == PARAM_MODE_IMMEDIATE) {
            val out = memory[instructionPointer + 1]
            instructionPointer += 2
            return out
        }
        val target = memory[instructionPointer + 1]
        if (target >= memory.size) throw ExecutionFailed("Out of bounds")
        val output = memory[target]
        instructionPointer += 2
        return output
    }

    private fun opJump(paramModes: Int, target: Boolean) {
        val (pm2, pm1) = paramModes.toString().padStart(2, '0').map { it.toString().toInt() }
        val operand1 = memory[instructionPointer + 1]
        val o1value = if (pm1 == PARAM_MODE_POSITION) memory[operand1] else operand1

        if ((o1value != 0) == target) {
            val operand2 = memory[instructionPointer + 2]
            val o2value = if (pm2 == PARAM_MODE_POSITION) memory[operand2] else operand2
            instructionPointer = o2value
        } else {
            instructionPointer += 3
        }
    }

    private fun step(): StepResult {
        val instuction = memory[instructionPointer]
        val op = instuction % 100
        val paramModes = instuction / 100

        var output: Int? = null
        var input = false
        when (op) {
            OPCODE_HALT -> return StepResult(halted = true)
            OPCODE_ADD -> opAdd(paramModes)
            OPCODE_MUL -> opMul(paramModes)
            OPCODE_INPUT -> {
                if (!hasInput()) return StepResult(needsInput = true, halted = false)
                opInput();
                input = true
            }
            OPCODE_OUTPUT -> output = opOutput(paramModes)
            OPCODE_EQUALS -> opEquals(paramModes)
            OPCODE_LESS_THAN -> opLessThan(paramModes)
            OPCODE_JUMP_IF_TRUE -> opJump(paramModes, true)
            OPCODE_JUMP_IF_FALSE -> opJump(paramModes, false)
            else -> throw Exception("Unknown instruction: $op")
        }
        return StepResult(halted = false, output = output, readInput = input)
    }


    fun runUntilHalt(input: Collection<Int> = emptyList()): List<Int> {
        writeInput(input)
        var output = emptyMutableList<Int>()
        while (true) {
            val res = step()
            if (res.output != null) {
                output += res.output
            }
            if (res.needsInput) throw ExecutionFailed("Insufficient input to run fully")
            if (res.halted) break
        }
        return output.toList()
    }

    fun runUntilFirstOutput(): RunResult {
        while (true) {
            val res = step()
            if (res.output != null) {
                return RunResult(output = listOf(res.output), halted = res.halted)
            }
            if (res.halted) return RunResult(output = emptyList(), halted = res.halted)
        }
    }

    fun runUntilNeedsInputOrHalt(): RunResult {
        var output = emptyMutableList<Int>()
        while (true) {
            val res = step()
            if (res.output != null) {
                output += res.output
            }
            if (res.needsInput || res.halted) return RunResult(output = output, halted = res.halted)
        }
    }
}
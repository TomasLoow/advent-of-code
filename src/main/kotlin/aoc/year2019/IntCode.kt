package aoc.year2019

import aoc.utils.ExpandingArray
import aoc.utils.emptyMutableList

class ExecutionFailed(val reason: String = "") : Throwable()

const val OPCODE_ADD = 1L
const val OPCODE_MUL = 2L
const val OPCODE_INPUT = 3L
const val OPCODE_OUTPUT = 4L
const val OPCODE_JUMP_IF_TRUE = 5L
const val OPCODE_JUMP_IF_FALSE = 6L
const val OPCODE_LESS_THAN = 7L
const val OPCODE_EQUALS = 8L
const val OPCODE_RELATIVE_BASE = 9L
const val OPCODE_HALT = 99L

const val PARAM_MODE_POSITION = 0
const val PARAM_MODE_IMMEDIATE = 1
const val PARAM_MODE_RELATIVE = 2


private data class StepResult(
    val halted: Boolean,
    val output: Long? = null,
    val readInput: Boolean = false,
    val needsInput: Boolean = false
)

data class RunResult(val output: List<Long>, val halted: Boolean) {
    val outputString: String
        get() = this.output.map { it.toInt().toChar() }.joinToString("")
}


class IntCode(var startingMemory: Array<Long>, val name: String = "IntCode") {
        var memory: ExpandingArray<Long>

        init {
            memory = ExpandingArray(startingMemory.clone(), emptyValue = 0L)
        }

        var instructionPointer = 0
        var relativeBase = 0
        var inputBuffer: MutableList<Long> = emptyMutableList()

        fun clone() : IntCode {
            val c = IntCode(memory.a.clone(), name)
            c.instructionPointer = this.instructionPointer
            c.inputBuffer = this.inputBuffer.toMutableList()
            c.relativeBase = this.relativeBase
            return c
        }

        fun writeInput(i: Long) {
            inputBuffer.add(i)
        }

        fun writeInput(i: Collection<Long>) {
            i.forEach { writeInput(it) }
        }

        fun reset() {
            instructionPointer = 0
            relativeBase = 0
            inputBuffer.clear()
            memory = ExpandingArray(startingMemory.clone(), 0L)
        }

        private fun getValue(i: Long, paramMode: Int): Long {
            return when (paramMode) {
                PARAM_MODE_POSITION -> memory[i.toInt()]
                PARAM_MODE_IMMEDIATE -> i
                PARAM_MODE_RELATIVE -> memory[(i + relativeBase).toInt()]
                else -> throw ExecutionFailed("Unknown param mode: $paramMode")
            }
        }

        private fun getAddress(operand: Long, paramMode: Int): Long {
            return when (paramMode) {
                PARAM_MODE_IMMEDIATE -> throw ExecutionFailed("Cannot access address in immediate mode")
                PARAM_MODE_POSITION -> operand
                PARAM_MODE_RELATIVE -> (operand + relativeBase)
                else -> throw ExecutionFailed("Unknown param mode: $paramMode")
            }
        }

        private fun hasInput() = this.inputBuffer.isNotEmpty()

        private fun binaryOp(paramModes: Int, action: (Long, Long) -> Long) {
            val (pm3, pm2, pm1) = paramModes.toString().padStart(3, '0').map { it.toString().toInt() }
            val a = getValue(memory[instructionPointer + 1], pm1)
            val b = getValue(memory[instructionPointer + 2], pm2)
            val target = getAddress(memory[instructionPointer + 3], pm3)
            memory[target.toInt()] = action(a, b)
            instructionPointer += 4
        }

        private fun opAdd(paramModes: Int) = binaryOp(paramModes) { a, b -> a + b }
        private fun opMul(paramModes: Int) = binaryOp(paramModes) { a, b -> a * b }
        private fun opEquals(paramModes: Int) = binaryOp(paramModes) { a, b -> if (a == b) 1 else 0 }
        private fun opLessThan(paramModes: Int) = binaryOp(paramModes) { a, b -> if (a < b) 1 else 0 }

        private fun opInput(paramModes: Int) {
            val target = getAddress(memory[instructionPointer + 1], paramModes)
            val i = inputBuffer.removeFirst()
            memory[target.toInt()] = i
            instructionPointer += 2
        }

        private fun opOutput(paramModes: Int): Long {
            val operand = memory[instructionPointer + 1]
            val value = getValue(operand, paramModes)
            instructionPointer += 2
            return value
        }

        private fun opJump(paramModes: Int, target: Boolean) {
            val (pm2, pm1) = paramModes.toString().padStart(2, '0').map { it.toString().toInt() }
            val operand1 = memory[instructionPointer + 1]
            val o1value = getValue(operand1, pm1)

            if ((o1value != 0L) == target) {
                val operand2 = memory[instructionPointer + 2]
                val o2value = getValue(operand2, pm2)
                instructionPointer = o2value.toInt()
            } else {
                instructionPointer += 3
            }
        }

        private fun opSetRelative(paramMode: Int) {
            val operand1 = memory[instructionPointer + 1]
            val o1value = getValue(operand1, paramMode)
            relativeBase += o1value.toInt()
            instructionPointer += 2
        }


        private fun step(): StepResult {
            val instuction = memory[instructionPointer]
            val op = instuction % 100
            val paramModes = (instuction / 100).toInt()

            var output: Long? = null
            var input = false
            when (op) {
                OPCODE_HALT -> return StepResult(halted = true)
                OPCODE_ADD -> opAdd(paramModes)
                OPCODE_MUL -> opMul(paramModes)
                OPCODE_INPUT -> {
                    if (!hasInput()) return StepResult(needsInput = true, halted = false)
                    opInput(paramModes)
                    input = true
                }

                OPCODE_OUTPUT -> output = opOutput(paramModes)
                OPCODE_EQUALS -> opEquals(paramModes)
                OPCODE_LESS_THAN -> opLessThan(paramModes)
                OPCODE_JUMP_IF_TRUE -> opJump(paramModes, true)
                OPCODE_JUMP_IF_FALSE -> opJump(paramModes, false)
                OPCODE_RELATIVE_BASE -> opSetRelative(paramModes)
                else -> throw Exception("Unknown instruction: $op")
            }
            return StepResult(halted = false, output = output, readInput = input)
        }


        fun runUntilHalt(input: Collection<Long> = emptyList()): List<Long> {
            writeInput(input)
            val output = emptyMutableList<Long>()
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
            val output = emptyMutableList<Long>()
            while (true) {
                val res = step()
                if (res.output != null) {
                    output += res.output
                }
                if (res.needsInput || res.halted) return RunResult(output = output, halted = res.halted)
            }
        }
    }
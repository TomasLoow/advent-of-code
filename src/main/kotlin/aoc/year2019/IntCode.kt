package aoc.year2019

class ExecutionFailed : Throwable()

const val OPCODE_ADD = 1
const val OPCODE_MUL= 2
const val OPCODE_HALT = 99

class IntCode(var memory: Array<Int>) {
    var ptr = 0

    fun step(): Boolean {
        val op = memory[ptr]
        if (op == OPCODE_HALT) return false
        val operand1 = memory[ptr + 1]
        val operand2 = memory[ptr + 2]
        val operand3 = memory[ptr + 3]

        if (opUsesMemory(op) && (operand1 >= memory.size || operand2 >= memory.size || operand3 >= memory.size)) {
            throw ExecutionFailed()
        }
        when (op) {
            OPCODE_ADD -> memory[operand3] = memory[operand1] + memory[operand2]
            OPCODE_MUL -> memory[operand3] = memory[operand1] * memory[operand2]
            else -> throw Exception("Unknown instruction: $op")
        }
        ptr += 4
        return true
    }

    private fun opUsesMemory(instr: Int): Boolean {
        return instr == OPCODE_ADD || instr == OPCODE_MUL
    }

    fun run() {
        try {
            while (step()) {
                // running along...
            }
        } catch (e: ExecutionFailed) {
            return
        }
    }

}
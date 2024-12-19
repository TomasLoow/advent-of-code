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
        val operand_1 = memory[ptr + 1]
        val operand_2 = memory[ptr + 2]
        val operand_3 = memory[ptr + 3]

        if (opUsesMemory(op) && (operand_1 >= memory.size || operand_2 >= memory.size || operand_3 >= memory.size)) {
            throw ExecutionFailed()
        }
        when (op) {
            OPCODE_ADD -> memory[operand_3] = memory[operand_1] + memory[operand_2]
            OPCODE_MUL -> memory[operand_3] = memory[operand_1] * memory[operand_2]
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
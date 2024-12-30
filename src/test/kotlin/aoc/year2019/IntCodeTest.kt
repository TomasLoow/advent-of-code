package aoc.year2019

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IntCodeTest {

    @Test
    fun test_examples_day2() {
        var computer = IntCode(arrayOf(1, 0, 0, 0, 99))
        computer.runUntilHalt()
        assertEquals(listOf(2, 0, 0, 0, 99), computer.memory.toList())

        computer.memory = arrayOf(2, 3, 0, 3, 99)
        computer.instructionPointer = 0
        computer.runUntilHalt()
        assertEquals(listOf(2, 3, 0, 6, 99), computer.memory.toList())

        computer.memory = arrayOf(2, 4, 4, 5, 99, 0)
        computer.instructionPointer = 0
        computer.runUntilHalt()
        assertEquals(listOf(2, 4, 4, 5, 99, 9801), computer.memory.toList())

        computer.memory = arrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)
        computer.instructionPointer = 0
        computer.runUntilHalt()
        assertEquals(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99), computer.memory.toList())
    }

    @Test
    fun test_examples_day5_part1() {
        val computer = IntCode(arrayOf(1002, 4, 3, 4, 33))
        computer.runUntilHalt()
        assertEquals(listOf(1002, 4, 3, 4, 99), computer.memory.toList())

        computer.memory = arrayOf(3, 0, 4, 0, 99)
        computer.instructionPointer = 0
        assertEquals(listOf(7), computer.runUntilHalt(listOf(7)))

    }

    @Test
    fun test_examples_day5_part2_comparisons() {
        // input == 8
        var computer = IntCode(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8))
        assertEquals(listOf(1), computer.runUntilHalt(listOf(8)))
        computer.reset()
        assertEquals(listOf(0), computer.runUntilHalt(listOf(18)))

        // input < 8
        computer = IntCode(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8))
        assertEquals(listOf(1), computer.runUntilHalt(listOf(7)))
        computer.reset()
        assertEquals(listOf(0), computer.runUntilHalt(listOf(8)))

        // input == 8
        computer = IntCode(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99))
        assertEquals(listOf(1), computer.runUntilHalt(listOf(8)))
        computer.reset()
        assertEquals(listOf(0), computer.runUntilHalt(listOf(18)))

        // input < 8
        computer = IntCode(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99))
        assertEquals(listOf(1), computer.runUntilHalt(listOf(7)))
        computer.reset()
        assertEquals(listOf(0), computer.runUntilHalt(listOf(8)))
    }

    @Test
    fun test_examples_day5_part2_jumps() {
        // output 0 if the input was zero or 1 if the input was non-zero
        var computer = IntCode(arrayOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9))
        assertEquals(listOf(0), computer.runUntilHalt(listOf(0)))
        computer.reset()
        assertEquals(listOf(1), computer.runUntilHalt(listOf(110)))

        computer = IntCode(arrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1))
        assertEquals(listOf(0), computer.runUntilHalt(listOf(0)))
        computer.reset()
        assertEquals(listOf(1), computer.runUntilHalt(listOf(110)))
    }

    @Test
    fun test_examples_day5_part2_larger_example() {
        /*
        [...] uses an input instruction to ask for a single number.
        The program will then output 999 if the input value is below 8,
        output 1000 if the input value is equal to 8,
        or output 1001 if the input value is greater than 8
         */
        val computer = IntCode(arrayOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99
        ))
        assertEquals(listOf(999), computer.runUntilHalt(listOf(-15)))
        computer.reset()
        assertEquals(listOf(1000), computer.runUntilHalt(listOf(8)))
        computer.reset()
        assertEquals(listOf(1001), computer.runUntilHalt(listOf(12345)))
        computer.reset()


    }


    @Test
    fun test_multiple_inputs() {
        val computer = IntCode(arrayOf(3,5,3,6,99,0,0))
        computer.runUntilHalt(listOf(42,97))
        assertEquals(42, computer.memory[5])
        assertEquals(97, computer.memory[6])

        /* Try same program one step at the time using runUntilNeedsInputOrHalt */
        computer.reset()
        computer.writeInput(42)
        val res1 = computer.runUntilNeedsInputOrHalt()
        assertFalse(res1.halted, "Computer is not halted, it's wating for one more input")
        assertEquals(42, computer.memory[5])
        assertEquals(0, computer.memory[6])
        computer.writeInput(97)
        val res2 = computer.runUntilNeedsInputOrHalt()
        assertTrue(res2.halted, "Had enough input to reach the end of the program")
        assertEquals(42, computer.memory[5])
        assertEquals(97, computer.memory[6])
    }
}
package aoc.year2019

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class IntCodeTest {

    @Test
    fun test_examples_day2() {
        val computer = IntCode(arrayOf(1, 0, 0, 0, 99))
        computer.runFully()
        assertEquals(listOf(2, 0, 0, 0, 99), computer.memory.take(5))

        computer.memory = arrayOf(2, 3, 0, 3, 99)
        computer.ptr = 0
        computer.runFully()
        assertEquals(listOf(2, 3, 0, 6, 99), computer.memory.take(5))

        computer.memory = arrayOf(2, 4, 4, 5, 99, 0)
        computer.ptr = 0
        computer.runFully()
        assertEquals(listOf(2, 4, 4, 5, 99, 9801), computer.memory.take(6))

        computer.memory = arrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)
        computer.ptr = 0
        computer.runFully()
        assertEquals(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99), computer.memory.take(9))
    }

    @Test
    fun test_examples_day5_part1() {
        val computer = IntCode(arrayOf(1002, 4, 3, 4, 33))
        computer.runFully()
        assertEquals(listOf(1002, 4, 3, 4, 99), computer.memory.take(5))

        computer.memory = arrayOf(3, 0, 4, 0, 99)
        computer.ptr = 0
        assertEquals(listOf(7), computer.runStreaming(sequenceOf(7)).toList())

    }

    @Test
    fun test_examples_day5_part2_comparisons() {
        // input == 8
        var computer = IntCode(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8))
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(8)).toList())
        computer.reset()
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(18)).toList())

        // input < 8
        computer = IntCode(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8))
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(7)).toList())
        computer.reset()
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(8)).toList())

        // input == 8
        computer = IntCode(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99))
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(8)).toList())
        computer.reset()
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(18)).toList())

        // input < 8
        computer = IntCode(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99))
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(7)).toList())
        computer.reset()
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(8)).toList())
    }

    @Test
    fun test_examples_day5_part2_jumps() {
        // output 0 if the input was zero or 1 if the input was non-zero
        var computer = IntCode(arrayOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9))
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(0)).toList())
        computer.reset()
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(110)).toList())

        computer = IntCode(arrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1))
        assertEquals(listOf(0), computer.runStreaming(sequenceOf(0)).toList())
        computer.reset()
        assertEquals(listOf(1), computer.runStreaming(sequenceOf(110)).toList())
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
        assertEquals(listOf(999), computer.runStreaming(sequenceOf(-15)).toList())
        computer.reset()
        assertEquals(listOf(1000), computer.runStreaming(sequenceOf(8)).toList())
        computer.reset()
        assertEquals(listOf(1001), computer.runStreaming(sequenceOf(12345)).toList())
        computer.reset()


    }
}
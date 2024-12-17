package year2024

import aoc.year2024.Day17Problem
import aoc.year2024.Register
import aoc.year2024.State
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Tests {
    @Test
    fun test1() {
        val p = Day17Problem()
        val state = State(registers = Register(c = 9), ops = arrayOf(2, 6), pointer = 0)
        p.runUntilHalt(state)
        assertEquals(1, state.registers.b)
    }

    @Test
    fun test2() {
        val p = Day17Problem()
        val state = State(registers = Register(a = 10), ops = arrayOf(5, 0, 5, 1, 5, 4), pointer = 0)
        val output = p.runUntilHalt(state)
        assertEquals(listOf(0, 1, 2).map { it.toLong() }, output)
    }

    @Test
    fun test3() {
        val p = Day17Problem()
        val state = State(
            registers = Register(a = 2024), ops = arrayOf(
                0, 1,
                5, 4,
                3, 0
            ), pointer = 0
        )
        val output = p.runUntilHalt(state)
        assertEquals(listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0).map { it.toLong() }, output)
        assertEquals(0, state.registers.a)
    }

    @Test
    fun test4() {
        val p = Day17Problem()
        val state = State(registers = Register(b = 29), ops = arrayOf(1, 7), pointer = 0)
        p.runUntilHalt(state)
        assertEquals(26, state.registers.b)
    }

    @Test
    fun test5() {
        val p = Day17Problem()
        val state = State(registers = Register(b = 2024, c = 43690), ops = arrayOf(4, 0), pointer = 0)
        p.runUntilHalt(state)
        assertEquals(44354, state.registers.b)
    }

}
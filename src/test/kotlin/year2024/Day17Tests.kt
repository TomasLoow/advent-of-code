package year2024

import aoc.year2024.Day17Problem
import aoc.year2024.Register
import aoc.year2024.State
import kotlinx.io.files.FileNotFoundException
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17Tests {
    @Test
    fun test1() {
        val p = Day17Problem()
        val state = State(registers = Register(c = 9), ops = listOf(2, 6), pointer = 0)
        p.runAsSequence(state).toList()
        assertEquals(1, state.registers.b)
    }

    @Test
    fun test2() {
        val p = Day17Problem()
        val state = State(registers = Register(a = 10), ops = listOf(5, 0, 5, 1, 5, 4), pointer = 0)
        val output = p.runAsSequence(state).toList()
        assertEquals(listOf(0, 1, 2).map { it.toLong() }, output)
    }

    @Test
    fun test3() {
        val p = Day17Problem()
        val state = State(
            registers = Register(a = 2024), ops = listOf(0, 1, 5, 4, 3, 0), pointer = 0
        )
        val output = p.runAsSequence(state).toList()
        assertEquals(listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0).map { it.toLong() }, output)
        assertEquals(0, state.registers.a)
    }

    @Test
    fun test4() {
        val p = Day17Problem()
        val state = State(registers = Register(b = 29), ops = listOf(1, 7), pointer = 0)
        p.runAsSequence(state).toList()
        assertEquals(26, state.registers.b)
    }

    @Test
    fun test5() {
        val p = Day17Problem()
        val state = State(registers = Register(b = 2024, c = 43690), ops = listOf(4, 0), pointer = 0)
        p.runAsSequence(state).toList()
        assertEquals(44354, state.registers.b)
    }

    @Test
    fun testPart2() {
        val p = Day17Problem()
        p.testData = false
        try {
            p.commonParts()
            val a = p.part2().toLong()
            val res = p.runForA(a)
            assertEquals(res, p.program)
        } catch (e: FileNotFoundException) {
            println("The test for 2024 Day 17 part 2doesn't work with the test input. Download your input file and it will run")
        }

    }
}
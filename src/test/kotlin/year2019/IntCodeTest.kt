import aoc.year2019.IntCode
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

internal class IntCodeTest {

    @Test
    fun test_examples_day2() {
        val computer = IntCode(arrayOf(1, 0, 0, 0, 99))
        computer.run()
        assertEquals(listOf(2, 0, 0, 0, 99), computer.memory.toList())

        computer.memory = arrayOf(2, 3, 0, 3, 99)
        computer.ptr = 0
        computer.run()
        assertEquals(listOf(2, 3, 0, 6, 99), computer.memory.toList())

        computer.memory = arrayOf(2, 4, 4, 5, 99, 0)
        computer.ptr = 0
        computer.run()
        assertEquals(listOf(2, 4, 4, 5, 99, 9801), computer.memory.toList())

        computer.memory = arrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)
        computer.ptr = 0
        computer.run()
        assertEquals(listOf(30, 1, 1, 4, 2, 5, 6, 0, 99), computer.memory.toList())
    }
}
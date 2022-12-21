package aoc.year2022

import org.junit.Test
import kotlin.test.assertEquals

class Day21KtTest {

    @Test
    fun `test reverseOp`() {
        assertEquals(10, reverseMathOp(5, MathOp.ADD, 15, true))  // 5+x=15
        assertEquals(10, reverseMathOp(5, MathOp.ADD, 15, false)) // x+5=15

        assertEquals(10, reverseMathOp(25, MathOp.SUB, 15, true)) // 25-x=15
        assertEquals(20, reverseMathOp(5, MathOp.SUB, 15, false))  // x-5=15

        assertEquals(3, reverseMathOp(5, MathOp.MUL, 15, true))  // 5*x=15
        assertEquals(3, reverseMathOp(5, MathOp.MUL, 15, false)) // x*5=15

        assertEquals(91, reverseMathOp(7, MathOp.DIV, 13, false))  // x/7=13
        assertEquals(5, reverseMathOp(25, MathOp.DIV, 5, true)) // 25/x=5


    }

}
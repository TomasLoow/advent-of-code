package aoc.utils

import aoc.utils.algorithms.hasLoop
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class TestLoopDetector {

    @Test
    fun `test on finite sequence`() {
        val seq = listOf(1, 2, 3, 4, 5).asSequence()

        assertFalse(hasLoop(seq))

    }

    @Test
    fun `test on loop`() {
        val seq = sequence {
            while(true) {
                yieldAll(listOf(1,2,3,4,5,6,7,8,9,11))
            }
        }

        assertTrue(hasLoop(seq))
    }

    @Test
    fun `test on shortest loop`() {
        val seq = sequence {
            while(true) {
                yield(1)
            }
        }

        assertTrue(hasLoop(seq))
    }

    @Test
    fun `test on eventual loop`() {
        val seq = sequence {
            yieldAll(listOf(99,87,65,43,21,22,44))
            while(true) {
                yieldAll(listOf(1,2,3,4,5,6,7,8,9,11))
            }
        }

        assertTrue(hasLoop(seq))
    }
}
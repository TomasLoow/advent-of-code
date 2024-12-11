package aoc.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.test.Test

class ExtensionFunctionsKtTest {

    @Test
    fun `test iterate`() {
        val addOne = { it: Int -> it + 1 }
        assertEquals(1, addOne.iterate(0, 1))
        assertEquals(100, addOne.iterate(0, 100))
        assertEquals(0, addOne.iterate(0, 0))

        fun dupString(s: String): String {
            return s + s
        }

        assertEquals("<->", ::dupString.iterate("<->", 0))
        assertEquals("<-><-><-><->", ::dupString.iterate("<->", 2))

    }


    @Test
    fun `test intersectRange`() {
        val range1 = (0..100)
        val range2 = (50..60)
        val range3 = (70..120)
        assertTrue { range1.intersectRange(range2) }
        assertTrue { range2.intersectRange(range1) }
        assertTrue { range1.intersectRange(range3) }
        assertTrue { range3.intersectRange(range1) }
        assertTrue { !range2.intersectRange(range3) }
        assertTrue { !range3.intersectRange(range2) }

    }

    @Test
    fun `test totalLengthOfCovered`() {
        assertEquals(5, listOf((1..5), (2..5), (4..5), (5..5)).totalLengthOfCovered())
        assertEquals(10, listOf((1..5), (6..10)).totalLengthOfCovered())
        assertEquals(10, listOf((1..5), (5..10)).totalLengthOfCovered())
        assertEquals(10, listOf((1..5), (7..11)).totalLengthOfCovered())
        assertEquals(11, listOf((1..5), (2..9), (7..11)).totalLengthOfCovered())
    }

    @Test
    fun `test concat long`() {
        repeat(100000) {
            val x = (Math.random()*1_000_000).roundToLong()
            val y = (Math.random()*1_000_000).roundToLong()
            assertEquals((x.toString()+y.toString()).toLong(), x.concat(y))
        }
    }

    @Test
    fun `test concat int`() {
        repeat(100000) {
            val x = (Math.random()*10_000).roundToInt()
            val y = (Math.random()*10_000).roundToInt()
            assertEquals((x.toString()+y.toString()).toInt(), x.concat(y))
        }
    }

    @Test
    fun `test permutationsSequence`() {
        val perms =  listOf(1,2,3).permutationsSequence().toSet()
        assertEquals(6, perms.size)
        assertTrue(listOf(1,2,3) in perms)
        assertTrue(listOf(1,3,2) in perms)
        assertTrue(listOf(2,1,3) in perms)
        assertTrue(listOf(2,3,1) in perms)
        assertTrue(listOf(3,1,2) in perms)
        assertTrue(listOf(3,2,1) in perms)
    }

    @Test
    fun `test empty permutationsSequence`() {
        val perms =  listOf<Int>().permutationsSequence().toSet()
        assertEquals(1, perms.size)
        assertTrue(listOf() in perms)
    }

    @Test
    fun `test larger permutationsSequence`() {
        // Works fine up to 10 or 11 somewhere.
        val perms =  (1..10).toList().permutationsSequence()
        val expectedCount = (1..10).toList().product()
        assertEquals(expectedCount, perms.count())
    }
}
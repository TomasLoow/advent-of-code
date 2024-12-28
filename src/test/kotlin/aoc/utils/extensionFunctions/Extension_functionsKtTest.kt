package aoc.utils.extensionFunctions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.test.Test
import kotlin.test.assertFalse

class MiscKtTest {

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

    @Test
    fun `test isAscending for integers`() {
        assertTrue(listOf(1, 2, 3, 4, 5).isAscending())
        assertTrue(listOf(1, 2, 3, 4, 5).isStrictlyAscending())

        assertTrue(listOf(5,4,3,2,1).isStrictlyDescending())
        assertTrue(listOf(5,4,3,2,1).isDescending())
        assertTrue(listOf(5,4,3,2,2).isDescending())
        assertFalse(listOf(5,4,3,2,2).isStrictlyDescending())

        assertTrue(listOf(2, 2, 3, 3, 5).isAscending())
        assertFalse(listOf(2, 2, 3, 3, 5).isStrictlyAscending())

        assertTrue(listOf(1).isAscending())
        assertTrue(emptyList<Int>().isAscending())
        assertTrue(listOf(1, 2, 2, 3).isAscending())
        assertFalse(listOf(1, 2, 2, 3).isStrictlyAscending())

        assertFalse(listOf(5, 4, 3, 2, 1).isAscending())
        assertFalse(listOf(1, 3, 2, 4, 5).isAscending())
    }

    @Test
    fun `test isAscending for strings`() {
        assertTrue("abc".isAscending())
        assertTrue("a".isAscending())
        assertTrue("".isAscending())
        assertTrue("aabbcc".isAscending())
        assertFalse("aabbcc".isStrictlyAscending())
        assertFalse("cba".isAscending())
        assertTrue("cba".isDescending())
        assertFalse("abdc".isAscending())
        assertFalse("abdc".isDescending())

        assertTrue("dcccba".isDescending())
        assertFalse("dcccba".isStrictlyDescending())

    }


}
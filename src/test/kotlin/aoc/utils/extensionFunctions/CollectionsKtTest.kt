package aoc.utils.extensionFunctions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CollectionsKtTest {

    @Test
    fun `test rotateRight and rotateLeft with an empty list`() {
        val emptyList = emptyList<Int>()
        assertEquals(emptyList, emptyList.rotateLeft(7))
        assertEquals(emptyList, emptyList.rotateRight(7))
    }

    @Test
    fun `test rotateRight and rotateLeft with positive rotation value`() {
        val list = listOf(1, 2, 3, 4, 5)
        assertEquals(listOf(4, 5, 1, 2, 3), list.rotateRight(2))
        assertEquals(listOf(3, 4, 5, 1, 2), list.rotateLeft(2))
    }


    @Test
    fun `test permutationsSequence`() {
        val perms = listOf(1, 2, 3).permutationsSequence().toSet()
        Assertions.assertEquals(6, perms.size)
        assertTrue(listOf(1, 2, 3) in perms)
        assertTrue(listOf(1, 3, 2) in perms)
        assertTrue(listOf(2, 1, 3) in perms)
        assertTrue(listOf(2, 3, 1) in perms)
        assertTrue(listOf(3, 1, 2) in perms)
        assertTrue(listOf(3, 2, 1) in perms)
    }

    @Test
    fun `test empty permutationsSequence`() {
        val perms = listOf<Int>().permutationsSequence().toSet()
        Assertions.assertEquals(1, perms.size)
        assertTrue(listOf() in perms)
    }

    @Test
    fun `test larger permutationsSequence`() {
        // Works fine up to 10 or 11 somewhere.
        val perms = (1..10).toList().permutationsSequence()
        val expectedCount = (1..10).toList().product()
        Assertions.assertEquals(expectedCount, perms.count())
    }


    @Test
    fun `test isAscending for integers`() {
        assertTrue(listOf(1, 2, 3, 4, 5).isAscending())
        assertTrue(listOf(1, 2, 3, 4, 5).isStrictlyAscending())

        assertTrue(listOf(5, 4, 3, 2, 1).isStrictlyDescending())
        assertTrue(listOf(5, 4, 3, 2, 1).isDescending())
        assertTrue(listOf(5, 4, 3, 2, 2).isDescending())
        assertFalse(listOf(5, 4, 3, 2, 2).isStrictlyDescending())

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
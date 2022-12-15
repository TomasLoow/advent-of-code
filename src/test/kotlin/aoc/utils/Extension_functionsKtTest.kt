package aoc.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.assertTrue

class Extension_functionsKtTest {

    @Test
    fun iterate() {
        val addOne = { it:Int -> it + 1}
        assertEquals(1, addOne.iterate(0,1))
        assertEquals(100, addOne.iterate(0,100))
        assertEquals(0, addOne.iterate(0, 0))

        fun dupString(s: String): String {
            return s + s
        }

        assertEquals("<->", ::dupString.iterate("<->", 0))
        assertEquals("<-><-><-><->", ::dupString.iterate("<->", 2))

    }

    @Test
    fun intersectRange() {
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
}
package aoc.utils

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Extension_functionsKtTest {

    @Test
    fun iterate() {
        val addOne = { it:Int -> it + 1}
        assertEquals(1, addOne.iterate(0,1))
        assertEquals(100, addOne.iterate(0,100))
        assertEquals(0, addOne.iterate(0,0))

        fun dupString(s: String) : String {
            return s+s
        }

        assertEquals("<->", ::dupString.iterate("<->",0))
        assertEquals("<-><-><-><->", ::dupString.iterate("<->",2))

    }
}
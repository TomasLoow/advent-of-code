package aoc.utils.extensionFunctions

import org.junit.jupiter.api.Assertions
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.test.Test
import kotlin.test.assertEquals

class NumbersKtTest {
    @Test
    fun `test concat long`() {
        repeat(100000) {
            val x = (Math.random() * 1_000_000).roundToLong()
            val y = (Math.random() * 1_000_000).roundToLong()
            Assertions.assertEquals((x.toString() + y.toString()).toLong(), x.concat(y))
        }
    }

    @Test
    fun `test concat int`() {
        repeat(100000) {
            val x = (Math.random() * 10_000).roundToInt()
            val y = (Math.random() * 10_000).roundToInt()
            Assertions.assertEquals((x.toString() + y.toString()).toInt(), x.concat(y))
        }
    }

    @Test
    fun `test toInt`() {
        assertEquals(0, arrayOf<Boolean>().toInt(), "Empty array should convert to 0")
        assertEquals(0, arrayOf<Boolean>(false).toInt(), "'0' should convert to 0")
        assertEquals(5, arrayOf<Boolean>(true, false, true).toInt(), "'101' should convert to 5")
        assertEquals(5, arrayOf<Boolean>(true, false, true).toInt(), "'0101' should convert to 5")

    }

    @Test
    fun `test toInt and toBinaryArray inverses`() {
        repeat(1000) {
            val i = (Math.random() * 1_000_000).roundToInt()
            assertEquals(
                i,
                i.toBinaryArray().toInt(),
                "toBinaryArray and toInt should be inverses for random integer $i"
            )
        }
    }

    @Test
    fun `test toDecimalList`() {
        Assertions.assertEquals(listOf(1), 1.toDecimalList())
        Assertions.assertEquals(listOf(1, 0), 10.toDecimalList())
        Assertions.assertEquals(listOf(1, 2, 3), 123.toDecimalList())
        Assertions.assertEquals(listOf(9, 9, 9, 9), 9999.toDecimalList())
        Assertions.assertEquals(listOf(0), 0.toDecimalList())
    }

    @Test
    fun `test toDecimalList is inverse of parseDecimalList`() {
        repeat(1000) {
            val i = (Math.random() * 1_000_000).roundToInt()
            Assertions.assertEquals(i, i.toDecimalList().parseDecimalList())
        }
    }

}
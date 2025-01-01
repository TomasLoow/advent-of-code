package aoc.utils.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.math.BigInteger

class ChineseRemainderKtTest {

    @Test
    fun `test chineseRemainder with BigInteger inputs`() {
        // Single congruence
        val result1 = chineseRemainder(
            listOf(Pair(BigInteger.valueOf(7), BigInteger.valueOf(4)))
        )
        assertEquals(BigInteger.valueOf(4), result1)

        // Multiple congruences
        val result2 = chineseRemainder(
            listOf(
                Pair(BigInteger.valueOf(3), BigInteger.valueOf(2)),
                Pair(BigInteger.valueOf(5), BigInteger.valueOf(3)),
                Pair(BigInteger.valueOf(7), BigInteger.valueOf(2))
            )
        )
        assertEquals(BigInteger.valueOf(23), result2)

        // Empty list
        assertFailsWith<IllegalArgumentException> {
            chineseRemainder(emptyList<Pair<BigInteger,BigInteger>>())
        }
    }

    @Test
    fun `test chineseRemainder with Long inputs`() {
        // Single congruence
        val result1 = chineseRemainder(
            listOf(7L to 4L)
        )
        assertEquals(4L, result1)

        // Multiple congruences
        val result2 = chineseRemainder(
            listOf(
                3L to 2L,
                5L to 3L,
                7L to 2L
            )
        )
        assertEquals(23L, result2)

        // Empty list
        assertFailsWith<IllegalArgumentException> {
            chineseRemainder(emptyList<Pair<Long,Long>>())
        }
    }
}
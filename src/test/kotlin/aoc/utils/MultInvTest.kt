package aoc.utils

import aoc.utils.math.multInv
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigInteger


class MultInvTest {
    @Test
    fun `test multInv with valid input`() {
        var a = BigInteger.valueOf(3)
        var b = BigInteger.valueOf(11)
        assertEquals(BigInteger.ONE, (a*a.multInv(b)) % b)
        a = BigInteger.valueOf(56)
        b = BigInteger.valueOf(101)
        assertEquals(BigInteger.ONE, (a*a.multInv(b)) % b)
        a = BigInteger.valueOf(1)
        b = BigInteger.valueOf(29)
        assertEquals(BigInteger.ONE, (a*a.multInv(b)) % b)
        a = BigInteger("9876543210123456789")
        b = BigInteger("11223344556677889911")
        assertEquals(BigInteger.ONE, (a*a.multInv(b)) % b)
    }

    @Test
    fun `test multInv with no inverse existing`() {
        val a = BigInteger.valueOf(6)
        val b = BigInteger.valueOf(9)
        // Since gcd(6, 9) != 1, the inverse does not exist. Expect an IllegalArgumentException.
        assertThrows(ArithmeticException::class.java) {
            a.multInv(b)
        }
    }
}
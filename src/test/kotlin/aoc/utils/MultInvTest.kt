package aoc.utils

import aoc.utils.math.multInv
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigInteger
import java.util.stream.Stream


class MultInvTest {

    @ParameterizedTest(name = "Int MultInv of {0} mod {1}")
    @MethodSource("testCases")
    fun `test multInv with valid Int input`(a:Int,b:Int) {
        assertEquals(1, (a*a.multInv(b)) % b)
    }

    @ParameterizedTest(name = "Long MultInv of {0} mod {1}")
    @MethodSource("testCases")
    fun `test multInv with valid Long input`(a:Int,b:Int) {
        assertEquals(1.toLong(), (a.toLong()*a.toLong().multInv(b.toLong())) % b.toLong())
    }

    @ParameterizedTest(name = "BigInteger MultInv of {0} mod {1}")
    @MethodSource("testCases")
    fun `test multInv with valid BigInteger input`(a:Int,b:Int) {
        assertEquals(BigInteger.ONE, (a.toBigInteger()*a.toBigInteger().multInv(b.toBigInteger())) % b.toBigInteger())
    }

    @Test
    fun `test multInv with valid really big BigInteger input`() {
        val a = "16543645682317268464564553817236518723".toBigInteger()
        val b = "783462398476928347621398545454762394600".toBigInteger()
        assertEquals(BigInteger.ONE, (a * a.multInv(b)) % b)
    }


    @Test
    fun `test multInv with no inverse existing`() {
        // Since gcd(6, 9) != 1, the inverse does not exist. Expect an IllegalArgumentException.
        assertThrows(ArithmeticException::class.java) {
            6.toBigInteger().multInv(9.toBigInteger())
        }
        assertThrows(ArithmeticException::class.java) {
            6L.multInv(9L)
        }
        assertThrows(ArithmeticException::class.java) {
            6.multInv(9)
        }
    }

    private companion object {
        @JvmStatic
        fun testCases(): Stream<Arguments> = Stream.of(
            Arguments.of( 3,11),
            Arguments.of( 56,101),
            Arguments.of( 1,29),
            Arguments.of( 123456,55),
        )
    }

}
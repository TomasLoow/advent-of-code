package aoc.utils.math

import java.math.BigInteger

/**
 * Calculates the modular multiplicative inverse such that `(this * this.multInv(b)) % b == 1`.
 * The inputs `this` and `b` should both be greater than zero.
 *
 * @param b the modulus with respect to which the inverse is calculated
 * @return the modular multiplicative inverse of `this` modulo `b`
 */
fun Int.multInv(b: Int): Int {
    check(this > 0 && b > 0)
    var previousValue = 0
    var currentValue = 1

    var dividend = this
    var divisor = b

    while (dividend > 1) {
        val quotient = dividend / divisor
        val remainder = dividend % divisor
        dividend = divisor
        divisor = remainder
        val nextValue = currentValue - quotient * previousValue
        currentValue = previousValue
        previousValue = nextValue
    }
    if (currentValue < 0) currentValue += b
    return currentValue
}

/**
 * Calculates the modular multiplicative inverse such that `(this * this.multInv(b)) % b == 1`.
 * The inputs `this` and `b` should both be greater than zero.
 *
 * @param b the modulus with respect to which the inverse is calculated
 * @return the modular multiplicative inverse of `this` modulo `b`
 */
fun Long.multInv(b: Long): Long {
    check(this > 0L && b > 0L)
    var previousValue = 0L
    var currentValue = 1L

    var dividend = this
    var divisor = b

    while (dividend > 1L) {
        val quotient = dividend / divisor
        val remainder = dividend % divisor
        dividend = divisor
        divisor = remainder
        val nextValue = currentValue - quotient * previousValue
        currentValue = previousValue
        previousValue = nextValue
    }
    if (currentValue < 0L) currentValue += b
    return currentValue
}

/**
 * Calculates the modular multiplicative inverse such that `(this * this.multInv(b)) % b == 1`.
 * The inputs `this` and `b` should both be greater than zero.
 *
 * @param b the modulus with respect to which the inverse is calculated
 * @return the modular multiplicative inverse of `this` modulo `b`
 */
fun BigInteger.multInv(b: BigInteger): BigInteger {
    check(this > BigInteger.ZERO && b > BigInteger.ZERO)
    var previousValue = BigInteger.ZERO
    var currentValue = BigInteger.ONE

    var dividend = this
    var divisor = b

    while (dividend > BigInteger.ONE) {
        val quotient = dividend / divisor
        val remainder = dividend % divisor
        dividend = divisor
        divisor = remainder
        val nextValue = currentValue - quotient * previousValue
        currentValue = previousValue
        previousValue = nextValue
    }
    if (currentValue < BigInteger.ZERO) currentValue += b
    return currentValue
}
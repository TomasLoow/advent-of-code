@file:Suppress("unused")
package aoc.utils

import aoc.utils.extensionFunctions.product
import java.math.BigInteger

fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun gcd(a: BigInteger, b: BigInteger): BigInteger {
    return if (b == BigInteger.ZERO) a else gcd(b, a % b)
}

fun lcm(a: Int, b: Int): Int {
    return (a*b)/gcd(a,b)
}

fun lcm(a: Long, b: Long): Long {
    return (a*b)/gcd(a,b)
}

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return (a*b)/gcd(a,b)
}


/**
 * Calculates the modular multiplicative inverse such that `(this * this.multInv(b)) % b == 1`.
 * The inputs `this` and `b` should both be greater than zero.
 *
 * @param b the modulus with respect to which the inverse is calculated
 * @return the modular multiplicative inverse of `this` modulo `b`
 */
fun Int.multInv(b: Int): Int {
    assert(this > 0 && b > 0)
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
    assert(this > 0L && b > 0L)
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
    assert(this > BigInteger.ZERO && b > BigInteger.ZERO)
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


/**
 * Solves the Chinese remainder theorem for a given list of modulo and remainder pairs.
 * The function calculates the smallest non-negative solution that satisfies all given congruences.
 *
 * @param modulusAndRems A list of pairs, where each pair consists of a modulus (first)
 * and a corresponding remainder (second). Both values are of type BigInteger.
 *
 * @return The smallest non-negative solution as a BigInteger that satisfies all the
 * provided congruences. Ie `return-value % modulosAndRems[i].first == modulosAndRems[i].second` for all i
 */
fun chineseRemainder(modulusAndRems: List<Pair<BigInteger, BigInteger>>): BigInteger {
    val prod = modulusAndRems.map { it.first }.product()
    val sum = modulusAndRems
        .fold(BigInteger.ZERO) { acc, (mod, rem) ->
            val p = prod / mod
            acc + rem * p.multInv(mod) * p
        }
    return sum % prod
}
@file:Suppress("unused")
package aoc.utils.math

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
    return (a*b)/ gcd(a,b)
}

fun lcm(a: Long, b: Long): Long {
    return (a*b)/ gcd(a,b)
}

fun lcm(a: BigInteger, b: BigInteger): BigInteger {
    return (a*b)/ gcd(a,b)
}



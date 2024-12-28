@file:Suppress("unused")
package aoc.utils.extensionFunctions

import java.security.MessageDigest
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return HexFormat.of().formatHex(digest)
}


fun <A, B> Pair<A, B>.flip(): Pair<B, A> = Pair(this.second, this.first)


/** Apply a function to its own output a given number of times
 *
 * f.iterate(a,0) == a
 * f.iterate(a,1) == f(a)
 * f.iterate(a,n) == f(f.iterate(a, n-1))
 */
fun <A> ((A) -> A).iterate(init: A, steps: Int): A {
    return (1..steps).fold(init) { state, _ -> this(state) }
}


/** Finds the max and min of a collection of numbers in a single loop */
fun Iterable<Int>.minAndMax(): Pair<Int, Int> {
    return this.fold(Pair(Integer.MAX_VALUE, Integer.MIN_VALUE)) { (currentMin, currentMax), v ->
        Pair(
            min(currentMin, v),
            max(currentMax, v)
        )
    }
}

fun List<Int>.variance(): Double {
    val xavg = this.sum().toDouble() / this.size
    return this.sumOf { (it - xavg).pow(2) } / this.size
}


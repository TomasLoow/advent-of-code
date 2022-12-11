package aoc.utils

import java.io.File
import java.security.MessageDigest
import java.util.*
import kotlin.math.max

fun String.nonEmptyLines(): List<String> {
    // For some reason this is *much* faster than using isNotEmpty()
    return lines().filter { it.length > 0 }
}

fun File.readNonEmptyLines(): List<String> {
    return readText().nonEmptyLines()
}

fun String.ensureNl(): String {
    return replace("\r\n", "\n")
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return HexFormat.of().formatHex(digest)
}

fun IntRange.containsRange(range2: IntRange): Boolean {
    return range2.first in this && range2.last in this
}

fun IntRange.intersectRange(range2: IntRange): Boolean {
    return !(range2.last < start || range2.first > endInclusive)
}

/**
 * Parses a sequence of digits as an integer
 * listOf(1,3,7).parseDecimal() == 137
 */
fun Iterable<Int>.parseDecimal() : Int{
    return fold(0) {parsed, digit -> parsed*10 + digit }
}

fun Collection<Int>.product(): Int {
    return reduce { acc, i -> acc * i }
}
fun Collection<Long>.product(): Long {
    return reduce { acc, i -> acc * i }
}

fun <A, B> Pair<A, B>.flip(): Pair<B, A> = Pair(this.second, this.first)


/** Shifts array so element at idx+1 moves to idx,
 * sets last element to provided emptyValue.
 * Returns the head element that is pushed out of the array */
fun <T> Array<T>.shiftLeft(emptyValue:T): T {

    val head = this[0]
    repeat(this.size-1) { idx ->
        this[idx]=this[idx+1]
    }
    this[size-1] = emptyValue
    return head
}

fun <T> List<T>.permutationsSequence(): Sequence<List<T>> {
    if (this.isEmpty()) return sequenceOf(emptyList())
    val head = this.first()
    val tail = this.drop(1)
    return sequence {
        tail.permutationsSequence().forEach { perm ->
            repeat(perm.size + 1) { pos ->
                yield( perm.take(pos)+ listOf(head)+perm.drop(pos))
            }
        }
    }
}

/** Apply a function to its own output a given number of times
 *
 * f.iterate(a,0) == a
 * f.iterate(a,1) == f(a)
 * f.iterate(a,n) == f(f.iterate(a, n-1))
 */
fun <A> ((A)->A).iterate(init: A, steps:Int):A {
    return (1 .. steps).fold(init) { state, _ -> this(state) }
}


fun <K> MutableMap<K, Long>.increase(key: K, value: Long) {
    this[key] = getOrDefault(key, 0L) + value
}

fun <K> MutableMap<K, Int>.increase(key: K, value: Int) {
    this[key] = getOrDefault(key, 0) + value
}

fun Int.truncPositive(): Int {
    return max(0, this)
}
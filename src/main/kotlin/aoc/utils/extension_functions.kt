package aoc.utils

import java.io.File
import java.security.MessageDigest
import java.util.*

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

fun <A, B> Pair<A, B>.flip(): Pair<B, A> = Pair(this.second, this.first)

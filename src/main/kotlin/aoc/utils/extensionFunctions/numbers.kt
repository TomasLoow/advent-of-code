@file:Suppress("unused")
package aoc.utils.extensionFunctions

import kotlin.math.max

/**
 * Parses a sequence of digits as an integer
 * listOf(1,3,7).parseDecimalList() == 137
 */
fun Iterable<Int>.parseDecimalList(): Int {
    return fold(0) { parsed, digit -> parsed * 10 + digit }
}

/**
 * Expands a positive int to a list of its decimal digits.
 * 137.toDecimal() == listOf(1,3,7)
 */
fun Int.toDecimalList(): List<Int> {
    if (this < 0) throw Exception("undefined for negative numbers")
    var number = this

    return buildList {
        do {
            add(number % 10) // Lägg till den sista siffran.
            number /= 10                // Ta bort den sista siffran.
        } while (number > 0)
    }.reversed()
}

fun Collection<Int>.product(): Int {
    return reduce { acc, i -> acc * i }
}

/** Like product() but the result is biiiiig and is put in a Long */
fun Collection<Int>.productL(): Long {
    return fold(1L) { acc, i -> acc * i.toLong() }
}



fun Collection<Long>.product(): Long {
    return reduce { acc, i -> acc * i }
}

fun Int.truncPositive(): Int {
    return max(0, this)
}

val Int.even: Boolean
    get() = this % 2 == 0

val Int.odd: Boolean
    get() = this % 2 == 1

val Long.even: Boolean
    get() = this % 2L == 0L

val Long.odd: Boolean
    get() = this % 2L == 1L

/**
 * Concatenates the given Long value to the current Long as if appending digits.
 *
 * 123.concat(78) == 12378L
 *
 * Equivalent to (a.toString() + b.toString()).toLong() but faster.
 *
 * @param x The Long value to be concatenated to the current Long value.
 * @return The resulting Long value after concatenation.
 */
fun Long.concat(x: Long): Long {
    if (x < 0) throw Exception("Cannot concat a negative number")
    if (x < 10L) return this * 10L + x
    if (x < 100L) return this * 100L + x
    if (x < 1000L) return this * 1000L + x
    if (x < 10000L) return this * 10000L + x
    if (x < 100000L) return this * 100000L + x
    if (x < 1000000L) return this * 1000000L + x
    if (x < 10000000L) return this * 10000000L + x
    if (x < 100000000L) return this * 100000000L + x
    throw NotImplementedError("Add even more branches to Long.concat or do something more clever")
}

/**
 * Concatenates the given Int value to the current Int as if appending digits.
 *
 * 123.concat(78) == 12378
 *
 * Equivalent to (a.toString() + b.toString()).toInt() but faster.
 *
 * @param x The Long value to be concatenated to the current Long value.
 * @return The resulting Int value after concatenation.
 */
fun Int.concat(x: Int): Int {
    if (x < 0) throw Exception("Cannot concat a negative number")
    if (x < 10) return this * 10 + x
    if (x < 100) return this * 100 + x
    if (x < 1000) return this * 1000 + x
    if (x < 10000) return this * 10000 + x
    if (x < 100000) return this * 100000 + x
    if (x < 1000000) return this * 1000000 + x
    throw NotImplementedError("Add even more branches to Int.concat or do something more clever")
}

fun Int.toBinaryArray(): Array<Boolean> {
    if (this < 0) throw Exception("undefined for negative numbers")
    val binaryString = this.toString(2)
    return Array(binaryString.length) { binaryString[it] == '1' }
}


fun Array<Boolean>.toInt(): Int {
    return this.fold(0) { acc, bit -> (acc * 2) + if (bit) 1 else 0 }
}

fun Collection<Boolean>.toInt(): Int {
    return this.fold(0) { acc, bit -> (acc * 2) + if (bit) 1 else 0 }
}


/** Generates all the ways to decompose a number into a sum with a given length
 * 5.allSumsOfLength(3) = [[0,0,5],[0,1,4],[0,2,3],[0,3,2]],[0,4,1],[0,5,0],[1,0,4], ...]
 */
fun Int.allSumsOfLength(elements: Int): List<List<Int>> {
    if (elements == 1) {
        return listOf(listOf(this))

    }
    return buildList {
        (0..this@allSumsOfLength).forEach { v ->
            (this@allSumsOfLength - v).allSumsOfLength(elements - 1).forEach { rest ->
                add(rest + v)
            }
        }
    }
}


/**
 * Finds all subsets of the collection whose sum of elements equals the target value.
 *
 * @param target The target sum to find subsets for.
 * @return A list containing all subsets of the list whose elements sum up to the specified target.
 */
fun Collection<Int>.allSubsetsWithSum(target: Int): List<List<Int>> {
    if (target == 0) return listOf(listOf())
    if (this.size == 1) {
        return if (this.first() == target) listOf(listOf(target)) else listOf()
    }
    val current = this.first()
    val setWithout = this.drop(1)

    return setWithout.allSubsetsWithSum(target) + setWithout.allSubsetsWithSum(target-current).map { it + current }
}

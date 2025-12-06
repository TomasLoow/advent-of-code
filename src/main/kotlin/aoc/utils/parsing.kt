@file:Suppress("unused")

package aoc.utils

import aoc.utils.extensionFunctions.ensureNl
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.year2019.IntCode
import java.lang.Integer.parseInt
import java.lang.Long.parseLong


fun <A, B> parseTwoBlocks(data: String, parserA: (String) -> A, parserB: (String) -> B): Pair<A, B> {
    val (chunkA, chunkB) = data.ensureNl().split("\n\n").also { check (it.size == 2) { "Too many blocks" } }
    return Pair(parserA(chunkA), parserB(chunkB))
}

fun <A, B, C> parseThreeBlocks(
    data: String,
    parserA: (String) -> A,
    parserB: (String) -> B,
    parserC: (String) -> C
): Triple<A, B, C> {
    val (chunkA, chunkB, chunkC) = data.ensureNl().split("\n\n")
        .also { check(it.size == 3) { "Too many blocks" } }
    return Triple(parserA(chunkA), parserB(chunkB), parserC(chunkC))
}

fun parseAllPositiveInts(data: String): List<Int> {
    val findAllNumbers = Regex("""\d+""")
    return findAllNumbers.findAll(data).map { it.value.toInt() }.toList()
}

fun parseAllPositiveLongs(data: String): List<Long> {
    val findAllNumbers = Regex("""\d+""")
    return findAllNumbers.findAll(data).map { it.value.toLong() }.toList()
}

/**
 * Parses a file where the input is divided into multiple blocks separated by two newlines.
 * Each such block is parsed by the provided parser and a list of its results is returned
 */
fun <A> parseBlockList(data: String, parser: (String) -> A): List<A> {
    return data.ensureNl().split("\n\n").map(parser)
}

/**
 * Parses a file where each non-empty line is one single integer.
 */
fun parseIntLines(data: String): List<Int> {
    return data.nonEmptyLines().map(::parseInt)
}

/**
 * Parses a file where each non-empty line is one single (Long) integer.
 */
fun parseLongLines(data: String): List<Long> {
    return data.nonEmptyLines().map(::parseLong)
}

fun parseIntArray(data: String): Array2D<Int> {
    return Array2D.parseFromLines(data) { c -> c.digitToInt() }
}

fun parseCharArray(data: String): Array2D<Char> {
    return Array2D.parseFromLines(data, ::id)
}


/**
 * Parses a string where each line is a pair of values separated a separator that is given as a constant string. Any empty lines are ignored.
 *
 * Example.
 *
 * parseListOfPairs(s, ::parseInt, ::parseInt, ",") would parse the string
 * """1,2
 * 11,3
 * 1,4
 * """
 * returns listOf(Pair(1,2), Pair(11,3), Pair(1,4))
 */
fun <A, B> parseListOfPairs(
    inputText: String,
    component1parser: (String) -> A,
    component2parser: (String) -> B,
    separator: String = " "
): List<Pair<A, B>> {
    return inputText.nonEmptyLines().map { line ->
        val (a, b) = line.split(separator)
        Pair(component1parser(a), component2parser(b))
    }
}

/**
 * Parses a string where each line is a pair of values separated a separator that is given as a **Regex**. Any empty lines are ignored.
 *
 * Example
 *
 * parseListOfPairs(s, ::parseInt, ::parseInt, Regex("-"+)) would parse the string
 * """1-2
 * 11---3
 * 1--4"""
 * returns listOf(Pair(1,2), Pair(11,3), Pair(1,4))
 */
fun <A, B> parseListOfPairs(
    inputText: String,
    component1parser: (String) -> A,
    component2parser: (String) -> B,
    separator: Regex
): List<Pair<A, B>> {
    return inputText.nonEmptyLines().map { line ->
        val (a, b) = line.split(separator)
        Pair(component1parser(a), component2parser(b))
    }
}

/**
 * Parses a string where each line is a triple of values separated by separators separator that is given as a constant string. Any empty lines are ignored.
 *
 * Example.
 *
 * parseListOfTriples(s, ::id, ::parseInt, ::parseInt, "=", ",") would parse the string
 * """x=1,2
 * z=11,3
 * q=1,4
 * """
 * returns listOf(Triple("x", 1,2), Triple("z", 11,3), Triple("q", 1,4))
 */
fun <A, B, C> parseListOfTriples(
    inputText: String,
    component1parser: (String) -> A,
    component2parser: (String) -> B,
    component3parser: (String) -> C,
    separator1: String = " ",
    separator2: String = " "
): List<Triple<A, B, C>> {
    return inputText.nonEmptyLines().map { line ->
        val (a, rest) = line.split(separator1, limit = 2)
        val (b, c) = rest.split(separator2, limit = 2)
        Triple(component1parser(a), component2parser(b), component3parser(c))
    }

}

fun <A, B, C> parseListOfTriples(
    inputText: String,
    component1parser: (String) -> A,
    component2parser: (String) -> B,
    component3parser: (String) -> C,
    separator1: Regex = " ".toRegex(),
    separator2: Regex = " ".toRegex()
): List<Triple<A, B, C>> {
    return inputText.nonEmptyLines().map { line ->
        val (a, rest) = line.split(separator1)
        val (b, c) = rest.split(separator2)
        Triple(component1parser(a), component2parser(b), component3parser(c))
    }

}


fun <A> parseOneLineOfSeparated(data: String, parser: (String) -> A, s: String): List<A> {
    return data.nonEmptyLines().single().split(s).map { parser(it) }
}

fun <A> parseOneLineOfSeparated(data: String, parser: (String) -> A, r: Regex): List<A> {
    return data.nonEmptyLines().single().split(r).map { parser(it) }
}

/**
 * parseCoord("16,-7") == Coord(16,-7)
 */
fun parseCoord(data: String): Coord {
    val (x, y) = data.split(",")
    return Coord(x.toInt(), y.toInt())
}

fun parseDirectionFromArrow(it: Char): Direction {
    return when (it) {
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> {
            throw Exception("Bad input")
        }
    }
}

fun parseDirectionFromURDL(it: Char): Direction {
    return when (it) {
        'U' -> Direction.UP
        'R' -> Direction.RIGHT
        'D' -> Direction.DOWN
        'L' -> Direction.LEFT
        else -> {
            throw Exception("Bad input")
        }
    }
}

fun parseDirectionFromNESW(it: Char): Direction {
    return when (it) {
        'N' -> Direction.UP
        'E' -> Direction.RIGHT
        'S' -> Direction.DOWN
        'W' -> Direction.LEFT
        else -> {
            throw Exception("Bad input")
        }
    }
}


/**
 * Does nothing. Strangely enough, that can be useful sometimes.
 */
fun <T> id(t: T) = t

/**
 * Parsers for IntCode programs from year 2019
 */
fun parseIntCodeProgram(s: String) =
    parseOneLineOfSeparated(s.nonEmptyLines().first(), String::toLong, ",").toTypedArray()

fun parseIntCodeComputer(s: String) = IntCode(parseIntCodeProgram(s))
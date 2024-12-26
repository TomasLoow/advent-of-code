package aoc.utils

import java.lang.Integer.parseInt


fun <A,B> parseTwoBlocks(data: String, parserA : (String) -> A, parserB: (String) -> B) : Pair<A,B> {
    val (chunkA, chunkB) = data.ensureNl().split("\n\n").also { if (it.size != 2) throw Exception("Too many blocks") }
    return Pair(parserA(chunkA), parserB(chunkB))
}

fun <A,B,C> parseThreeBlocks(data: String, parserA : (String) -> A, parserB: (String) -> B, parserC: (String) -> C) : Triple<A,B,C> {
    val (chunkA, chunkB, chunkC) = data.ensureNl().split("\n\n").also { if (it.size != 3) throw Exception("Too many blocks") }
    return Triple(parserA(chunkA), parserB(chunkB), parserC(chunkC))
}

fun parseAllDigits(data:String) : List<Int> {
    val findAllNumbers = Regex("""\d+""")
    return findAllNumbers.findAll(data).map { it.value.toInt() }.toList()
}
/**
 * Parses a file where the input is divided into multiple blocks separated by two newlines.
 * Each such block is parsed by the provided parser and a list of its results is returned
 */
fun <A> parseBlockList(data: String, parser : (String) -> A) : List<A> {
    return data.ensureNl().split("\n\n").map(parser)
}

/**
 * Parses a file where each non-empty line is one single integers.
 */
fun parseIntLines(data: String) :List<Int> {
    return data.nonEmptyLines().map(::parseInt)
}

fun parseIntArray(data: String) : Array2D<Int> {
    return Array2D.parseFromLines(data) { c -> c.digitToInt() }
}

fun parseCharArray(data: String) : Array2D<Char> {
    return Array2D.parseFromLines(data, ::id)
}


/**
 * Parses a file where each line is a pair of values separated a separator that is given as a constant string. Any empty lines are ignored.
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
 * Parses a file where each line is a pair of values separated a separator that is given as a **Regex**. Any empty lines are ignored.
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


fun <A, B, C> parseListOfTriples(
    inputText: String,
    component1parser: (String) -> A,
    component2parser: (String) -> B,
    component3parser: (String) -> C,
    separator1: String = " ",
    separator2: String = " "
): List<Triple<A, B, C>> {
    return inputText.nonEmptyLines().map { line ->
        val (a, rest) = line.split(separator1, limit=2)
        val (b,c) = rest.split(separator2, limit=2)
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
        val (b,c) = rest.split(separator2)
        Triple(component1parser(a), component2parser(b), component3parser(c))
    }

}


fun <A> parseOneLineOfSeparated(data: String, parser: (String) -> A, s: String): List<A> {
    return data.nonEmptyLines().single().split(s).map { parser(it) }
}

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

fun <T> id(t:T) = t
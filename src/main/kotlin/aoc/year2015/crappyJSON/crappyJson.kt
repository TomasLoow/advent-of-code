@file:Suppress("unused")
package aoc.year2015.crappyJSON

import java.lang.Integer.parseInt

/*
Yes, writing my own JSON parser instead of just importing the standard one was a *terrible* idea.
But that's what I do.
*/
sealed interface JSON {
    class I(val int: Int) : JSON
    class S(val string: String) : JSON
    class A(val content: List<JSON>) : JSON
    class O(val content: Map<String, JSON>) : JSON

}

fun parseJInt(input: String): Pair<JSON.I, String> {
    return if (input.startsWith('-')) {
        val numPart = input.drop(1).takeWhile { it.isDigit() }
        Pair(JSON.I(-parseInt(numPart)), input.drop(numPart.length + 1))
    } else {
        val numPart = input.takeWhile { it.isDigit() }
        Pair(JSON.I(parseInt(numPart)), input.drop(numPart.length))

    }
}

fun parseJString(input: String): Pair<JSON.S, String> {
    val s = input.drop(1).takeWhile { it != '\"' }
    return Pair(JSON.S(s), input.drop(s.length + 2))
}

fun parseJArray(input: String): Pair<JSON.A, String> {
    if (input.startsWith("[]")) return Pair(JSON.A(emptyList()), input.drop(2))
    var inp = input.drop(1)
    var consumed = 1
    val content = buildList<JSON> {
        while (true) {
            val (j, unparsed) = parseJSON(inp)
            add(j)
            val consumedByElement = inp.length - unparsed.length
            if (unparsed.first() == ']') {
                inp = unparsed
                consumed += consumedByElement
                break
            }
            inp = unparsed.drop(1) // +1 from comma
            consumed += consumedByElement + 1
        }
        inp = inp.drop(1)
        consumed++
    }
    return Pair(JSON.A(content), inp)
}

fun parseJObject(input: String): Pair<JSON.O, String> {
    if (input.startsWith("{}")) return Pair(JSON.O(emptyMap()), input.drop(2))
    var inp = input.drop(1)
    var consumed = 1
    val content = buildMap<String, JSON> {
        while (true) {
            if (inp.first() == '}') break
            val (keyJ, unparsedAfterKey) = parseJString(inp)
            val consumedByKey = inp.length - unparsedAfterKey.length
            consumed += consumedByKey + 1
            inp = unparsedAfterKey.drop(1) // drop :

            val (valueJ, unparsedAfterValue) = parseJSON(inp)
            val consumedByValue = inp.length - unparsedAfterValue.length
            if (unparsedAfterValue.first() == '}') {
                inp = unparsedAfterValue.drop(1)
                consumed += consumedByValue + 1
                this[keyJ.string] = valueJ
                break
            }
            consumed += consumedByValue + 1
            inp = unparsedAfterValue.drop(1) // drop }
            this[keyJ.string] = valueJ
        }
    }
    return Pair(JSON.O(content), inp)
}


fun parseJSON(input: String): Pair<JSON, String> {
    when (input.first()) {
        in "-0123456789" -> return parseJInt(input)
        '\"' -> return parseJString(input)
        '[' -> return parseJArray(input)
        '{' -> return parseJObject(input)
    }
    throw Exception("parse error")
}

fun printJSON(j: JSON) {
    when (j) {
        is JSON.A -> {
            print("[")
            j.content.forEach { v ->
                printJSON(v)
                if (v != j.content.last()) print(",")
            }
            print("]")
        }

        is JSON.I -> print(j.int)
        is JSON.O -> {
            print("{")
            j.content.forEach { (k, v) ->
                print("\"$k\":")
                printJSON(v)
                if (v != j.content.values.last()) print(",")
            }
            print("}")
        }
        is JSON.S -> {
            print("\"")
            print(j.string)
            print("\"")
        }
    }
}
package aoc.year2015.crappyJSON

import aoc.utils.parseInt

/*
Yes, writing my own JSON parser instead of just importing the standard one was a *terrible* idea.
But that's what I do.
*/
sealed class JSON
class JSONInt(val int: Int) : JSON()
class JSONString(val string: String) : JSON()
class JSONArray(val content: List<JSON>) : JSON()
class JSONObject(val content: Map<String, JSON>) : JSON()

fun parseJSONInt(input: String): Pair<JSONInt, String> {
    return if (input.startsWith('-')) {
        val numPart = input.drop(1).takeWhile { it.isDigit() }
        Pair(JSONInt(-parseInt(numPart)), input.drop(numPart.length + 1))
    } else {
        val numPart = input.takeWhile { it.isDigit() }
        Pair(JSONInt(parseInt(numPart)), input.drop(numPart.length))

    }
}

fun parseJSONString(input: String): Pair<JSONString, String> {
    val s = input.drop(1).takeWhile { it != '\"' }
    return Pair(JSONString(s), input.drop(s.length + 2))
}

fun parseJSONArray(input: String): Pair<JSONArray, String> {
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
    return Pair(JSONArray(content), inp)
}

fun parseJSONObject(input: String): Pair<JSONObject, String> {
    var inp = input.drop(1)
    var consumed = 1
    val content = buildMap<String, JSON> {
        while (true) {
            if (inp.first() == '}') break
            val (keyJ, unparsedAfterKey) = parseJSONString(inp)
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
    return Pair(JSONObject(content), inp)
}


fun parseJSON(input: String): Pair<JSON, String> {
    when (input.first()) {
        in "-0123456789" -> return parseJSONInt(input)
        '\"' -> return parseJSONString(input)
        '[' -> return parseJSONArray(input)
        '{' -> return parseJSONObject(input)
    }
    throw Exception("parse error")
}

fun printJSON(j: JSON) {
    when (j) {
        is JSONArray -> {
            print("[")
            j.content.forEach { v ->
                printJSON(v)
                if (v != j.content.last()) print(",")
            }
            print("]")
        }

        is JSONInt -> print(j.int)
        is JSONObject -> {
            print("{")
            j.content.forEach { k, v ->
                print("\"$k\":")
                printJSON(v)
                if (v != j.content.values.last()) print(",")
            }
            print("}")
        }

        is JSONString -> {
            print("\"")
            print(j.string)
            print("\"")
        }
    }
}
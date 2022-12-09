package aoc.utils


fun <A,B> parseTwoBlocks(data: String, parserA : (String) -> A, parserB: (String) -> B) : Pair<A,B> {
    val (chunkA, chunkB) = data.ensureNl().split("\n\n").also { if (it.size != 2) throw Exception("Too many blocks") }
    return Pair(parserA(chunkA), parserB(chunkB))
}

fun <A,B,C> parseThreeBlocks(data: String, parserA : (String) -> A, parserB: (String) -> B, parserC: (String) -> C) : Triple<A,B,C> {
    val (chunkA, chunkB, chunkC) = data.ensureNl().split("\n\n").also { if (it.size != 3) throw Exception("Too many blocks") }
    return Triple(parserA(chunkA), parserB(chunkB), parserC(chunkC))
}

fun <A> parseBlockList(data: String, parser : (String) -> A) : List<A> {
    return data.ensureNl().split("\n\n").map(parser)
}


fun parseIntLines(data: String) :List<Int> {
    return data.nonEmptyLines().map { line -> line.toInt() }
}

fun parseIntArray(data: String) : Array2D<Int> {
    return Array2D.parseFromLines(data) { c -> c.digitToInt() }
}

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
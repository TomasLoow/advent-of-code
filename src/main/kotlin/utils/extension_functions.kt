package utils

fun String.nonEmptyLines(): List<String> {
    return lines().filter { it.isNotEmpty() }
}

fun IntRange.containsRange(range2: IntRange) : Boolean {
    return range2.start in this && range2.endInclusive in this
}

fun IntRange.intersectRange(range2: IntRange): Boolean {
    return ! (range2.endInclusive < start || range2.start > endInclusive)
}
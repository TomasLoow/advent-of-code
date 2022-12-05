package utils

fun String.nonEmptyLines(): List<String> {
    // For some reason this is faster than using isNotEmpty()
    return lines().filter { it.length > 0 }
}

fun IntRange.containsRange(range2: IntRange): Boolean {
    return range2.start in this && range2.endInclusive in this
}

fun IntRange.intersectRange(range2: IntRange): Boolean {
    return !(range2.endInclusive < start || range2.start > endInclusive)
}
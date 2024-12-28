package aoc.utils.extensionFunctions

val IntRange.length: Int
    get() {
        if (this.step != 1) throw NotImplementedError("TODO")
        return last - first + 1
    }

fun IntRange.containsRange(range2: IntRange): Boolean {
    return range2.first in this && range2.last in this
}

fun IntRange.intersectRange(range2: IntRange): Boolean {
    return !(range2.last < start || range2.first > endInclusive)
}

fun IntRange.intersectionOrNull(other: IntRange): IntRange? {
    val r = (Integer.max(this.first, other.first)..Integer.min(this.last, other.last))
    return if (r.isEmpty()) null else r
}

/** Returns the total length of a collection of IntRanges, points that are covered by more than one range are not counted twice */
fun Collection<IntRange>.totalLengthOfCovered(): Int {
    val endpoints: Map<Int, Int> = buildMap {  // Map of coordinates to depth changes
        this@totalLengthOfCovered.forEach { range ->
            this.increase(range.first, 1)
            this.increase(range.last, -1)
        }
    }
    val initial: Triple<Int, Int, Int?> = Triple(0, 0, null)
    val (_, c, _) = endpoints
        .toList()
        .filter { it.second != 0 }
        .sortedBy { it.first }
        .fold(initial) { (depth, count, posOfOpen), (pos, change) ->
            val newDepth = depth + change

            if (depth == 0) {
                Triple(change, count, pos)// record opening pos
            } else {
                if (newDepth == 0) {
                    Triple(0, count + (pos - posOfOpen!! + 1), null)
                } else {
                    Triple(newDepth, count, posOfOpen)
                }
            }
        }
    return c
}
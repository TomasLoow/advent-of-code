package aoc.utils.extensionFunctions

/** Shifts an array in place so that elements at idx+1 move to idx,
 * sets the last element to emptyValue.
 * Returns the head element which is 'pushed out' of the array */
fun <T> Array<T>.shiftLeft(emptyValue: T): T {

    val head = this[0]
    repeat(this.size - 1) { idx ->
        this[idx] = this[idx + 1]
    }
    this[size - 1] = emptyValue
    return head
}

/**
 * Rotates the elements of the list to the left by the specified number of positions.
 * Items that rotate out at the left end are placed at the right end.
 *
 * Crashes and burns without dignity if i is negative.
 *
 * @example listOf(1,2,3,4,5).rotateLeft(2) == listOf(3,4,5,6,1,2)
 * @param i The number of positions to rotate the list to the left. Must be non-negative.
 * @return A new list with elements rotated to the left by the specified number of positions.
 */
fun <T> List<T>.rotateLeft(i: Int): List<T> {
    if (isEmpty()) return this
    val r = i % size
    return drop(r) + take(r)
}

/**
 * Rotates the elements of the list to the right by the specified number of positions.
 * Items that rotate out at the right end are placed at the left end.
 *
 * Crashes and burns without dignity if i is negative.
 *
 * @example listOf(1,2,3,4,5).rotateRight(2) == listOf(4,5,6,1,2,3)
 * @param i The number of positions to rotate the list to the left. Must be non-negative.
 * @return A new list with elements rotated to the left by the specified number of positions.
 */
fun <T> List<T>.rotateRight(i: Int): List<T> {
    if (isEmpty()) return this
    val otherWay = size - i % size
    return rotateLeft(otherWay)
}

/**
 * Yields all permutations of a collection in some unspecified order
 *
 * listOf(1,2,3).permutationsSequence() == sequenceOf(listOf(1,2,3),listOf(1,3,2),listOf(2,1,3),listOf(2,3,1),listOf(3,1,2),listOf(3,2,1))
 */
fun <T> Collection<T>.permutationsSequence(): Sequence<List<T>> {
    if (this.isEmpty()) return sequenceOf(emptyList())
    val head = this.first()
    val tail = this.drop(1)
    return sequence {
        tail.permutationsSequence().forEach { perm ->
            repeat(perm.size + 1) { pos ->
                yield(perm.take(pos) + listOf(head) + perm.drop(pos))
            }
        }
    }
}

/**
 * Generates all possible subsets of the collection as a sequence of sets.
 * Note that this is 2^<size> elements, so don't use it on large collections.
 *
 * The empty set is always returned first, and the full set is always returned last,
 * but the elements between are *not* necessarily sorted by size.
 *
 * @return A sequence containing all subsets of the collection, including the empty set and the collection itself.
 */
fun <T> Collection<T>.subSets(): Sequence<Set<T>> {
    if (this.isEmpty()) return sequenceOf(emptySet())
    val head = this.first()
    val tail = this.drop(1)
    return sequence {
        val tailSubsets = tail.subSets()
        tailSubsets.forEach { ss ->
            yield(ss)
            yield(ss + head)
        }
    }
}

/**
 * Generates a sequence of all possible unordered pairs of elements from the iterable,
 * where each pair consists of two elements such that the second element
 * in the pair comes after the first element in the iterable.
 * if input contains a and b, then *one of* Pair(a,b) and Pair(b,a) will be produced but not both.
 * If want both, use allPairs() instead.
 *
 * Example: listOf(1,2,3,4) = sequenceOf(1 to 2, 1 to 3, 1 to 4, 2 to 3, 2 to 4, 3 to 4)
 */
fun <E> Iterable<E>.allUnorderedPairs(): Sequence<Pair<E, E>> {
    return sequence {
        this@allUnorderedPairs.forEachIndexed { index1, e ->
            this@allUnorderedPairs.drop(index1 + 1).forEach { e2 ->
                yield(e to e2)
            }
        }
    }
}

/**
 * Generates a sequence of all possible pairs of elements from the iterable, For two elements a and b at
 * different positions in the list both (a,b) and (b,a) are returned, but NOT (a,a) and (b,b).
 * If you want just one of these, use allUnorderedPairs() instead.
 *
 * If the list contains an element a more than once, then (a,a) will be included in the output though.
 *
 * Example: listOf(1,2,3) = sequenceOf(1 to 2, 1 to 3, 2 to 1, 2 to 3, 3 to 1, 3 to 2)
 */
fun <E> Iterable<E>.allPairs(): Sequence<Pair<E, E>> {
    return sequence {
        this@allPairs.forEachIndexed { index1, e1 ->
            this@allPairs.forEachIndexed { index2, e2 ->
                if (index1 != index2) yield(e1 to e2)
            }
        }
    }
}


fun <T : Comparable<T>> Iterable<T>.isAscending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a <= b }
}

fun <T : Comparable<T>> Iterable<T>.isStrictlyAscending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a < b }
}

fun <T : Comparable<T>> Iterable<T>.isDescending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a >= b }
}

fun <T : Comparable<T>> Iterable<T>.isStrictlyDescending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a > b }
}

fun Collection<Int>.countIncreases(): Long {
    return zipWithNext().count { it.first < it.second }.toLong()
}

/**
 * mirror the data across the diagonal
 * The output satisfies `∀ (x, y): input.mirrorDiagonally()[y][x] = input[x][y]`
 *
 * The input must be rectangular but need not be square.
 */
fun <T> List<List<T>>.mirrorDiagonally(): List<List<T>> {
    val width = this.first().size
    val height = this.size
    return (0 until width).map { y -> (0 until height).map { x -> this[x][y] } }
}
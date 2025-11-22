package aoc.utils

/**
 * A class for managing and dynamically resizing an array of `Long` values.
 *
 * It supports indexed access using `get` and `set` operators, automatically expanding
 * the array size when needed. When resizing the new array size is the smallest power of two that works is used.
 *
 * @constructor Initializes the array with the given `Long` array.
 * @property a The internal array that grows as needed.
 * */
class ExpandingArray<T>(var a: Array<T>, val emptyValue: T) : Collection<T> {

    operator fun get(i: Int): T {
        if (i < a.size) return a[i] else {
            enlarge(i)
            return this[i]
        }
    }

    operator fun set(i: Int, v: T) {
        if (i < a.size) a[i] = v else {
            enlarge(i)
            this[i] = v
        }
    }

    private fun enlarge(i: Int) {
        require(i >= a.size) { "New size must be larger than old size" }
        val newSize = (i + 1).takeHighestOneBit().shl(1) // next higher power of two
        @Suppress("UNCHECKED_CAST")
        val newArray = arrayOfNulls<Any>(newSize) as Array<T>
        a.forEachIndexed { index, l -> newArray[index] = l }
        for (j in a.size until newSize) {
            newArray[j] = emptyValue
        }
        a = newArray
    }

    override val size: Int
        get() = a.size

    override fun isEmpty(): Boolean {
        return a.isEmpty()
    }

    override fun contains(element: T): Boolean {
        return a.contains(element)
    }

    override fun iterator(): Iterator<T> {
        return a.iterator()
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all { contains(it) }
    }
}

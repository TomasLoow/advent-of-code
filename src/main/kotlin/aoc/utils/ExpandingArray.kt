package aoc.utils

import aoc.utils.extensionFunctions.toBinaryArray

/**
 * A class for managing and dynamically resizing an array of `Long` values.
 *
 * It supports indexed access using `get` and `set` operators, automatically expanding
 * the array size when needed. When resizing the new array size is the smallest power of two that works is used.
 *
 * @constructor Initializes the array with the given `Long` array.
 * @property a The internal array that grows as needed.
 * */
class ExpandingArray<T>(var a: Array<T>, val emptyValue: T) : Collection<T> by a.asList() {

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
        val newSize = 1.shl(i.toBinaryArray().size)
        @Suppress("UNCHECKED_CAST")
        val newArray = Array<Any?>(newSize) { emptyValue } as Array<T>
        a.forEachIndexed { index, l -> newArray[index] = l }
        a = newArray
    }
}
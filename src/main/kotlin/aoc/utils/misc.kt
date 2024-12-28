@file:Suppress("unused")
package aoc.utils

fun <K,V> emptyMutableMap(): MutableMap<K,V>  = mutableMapOf()
fun <E> emptyMutableSet() : MutableSet<E> { return  mutableSetOf() }
fun <E> emptyMutableList() : MutableList<E> { return  mutableListOf() }

/**
 * given a known "good" value and a known "bad" value, bisect until we find the highest good value and return it.
 *
 * @param function A function that returns true for good values returns false for bad values
 * @return the highest value i such that good <= i < bad && function(i) && !function(i+1)
 */
fun bisect(startGood: Int, startBad: Int, function: (Int) -> Boolean): Int {
    if (startGood >= startBad) TODO("bisect helper is only implemented for good < bad case")
    var g = startGood
    var b = startBad

    while (g + 1 < b) {
        val mid = (g + b) / 2
        if(function(mid)) {
            g = mid
        } else {
            b = mid
        }
    }
    return g
}
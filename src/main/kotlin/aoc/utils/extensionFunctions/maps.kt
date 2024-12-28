package aoc.utils.extensionFunctions

import aoc.utils.emptyMutableMap

/**
 * Replace the value at this[key] with mutator(this[key]), if key is not in the map, behave as if emptyValue was the value for this[key]
 */
fun <K, V> MutableMap<K, V>.mutate(key: K, emptyValue: V, mutator: (V) -> V) =
    if (key in this.keys) this[key] = mutator(this[key]!!) else this[key] = mutator(emptyValue)

/**
 * Perform some action with the value at this[key], if it is not in the map, initialize it with
 */
fun <K, V> MutableMap<K, V>.mutateImp(key: K, emptyValue: V, mutator: (V) -> Unit) {
    if (key !in this.keys) {
        this[key] = emptyValue
    }
    mutator(this[key]!!)
}

fun <K> MutableMap<K, Long>.increase(key: K, value: Long) = this.mutate(key, 0L) { it + value }
fun <K> MutableMap<K, Int>.increase(key: K, value: Int) = this.mutate(key, 0) { it + value }

/**
 * Creates a new map where keys and values of the original map are inverted.
 * Each value in the original map becomes a key in the new map,
 * and its corresponding value is a set containing all keys from the original map
 * that had the same value.
 *
 * @return A new map with inverted keys and values. Original map's values
 *         are the keys in the new map, and corresponding values are sets of
 *         keys from the original map.
 */
@SuppressWarnings("unused")
fun <K, V> Map<K, V>.invert(): Map<V, Set<K>> {
    val res = emptyMutableMap<V, Set<K>>()
    this.forEach { (k, v) ->
        val s = res.getOrDefault(v, emptySet())
        res[v] = s.plus(k)
    }
    return res
}
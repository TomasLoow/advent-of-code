@file:Suppress("unused")
package aoc.utils

fun <K,V> emptyMutableMap(): MutableMap<K,V>  = mutableMapOf()
fun <E> emptyMutableSet() : MutableSet<E> { return  mutableSetOf() }
fun <E> emptyMutableList() : MutableList<E> { return  mutableListOf() }

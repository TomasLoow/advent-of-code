package aoc.utils

// Why don't these already exist? Seriously?
// We have emptyList, listOf and mutableListOf but not emptyMutableList. Weird.
fun <K,V> emptyMutableMap(): MutableMap<K,V>  = mutableMapOf()
fun <E> emptyMutableSet() : MutableSet<E> { return  mutableSetOf() }
fun <E> emptyMutableList() : MutableList<E> { return  mutableListOf() }

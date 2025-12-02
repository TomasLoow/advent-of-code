package aoc.utils

import java.math.BigInteger

// Why don't these already exist? Seriously?
// We have emptyList, listOf and mutableListOf but not emptyMutableList. Weird.
fun <K,V> emptyMutableMap(): MutableMap<K,V>  = mutableMapOf()
fun <E> emptyMutableSet() : MutableSet<E> { return  mutableSetOf() }
fun <E> emptyMutableList() : MutableList<E> { return  mutableListOf() }

fun Iterable<BigInteger>.sum() = this.sumOf { it }
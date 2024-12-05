package aoc.utils

import java.util.ArrayDeque
import java.util.Deque
import java.util.LinkedList

/**
 * Determines if a sequence has a loop using Floyd’s Cycle Finding Algorithm
 *
 * @param seq the sequence of elements to be checked for the presence of a loop
 * @return true if the sequence contains a loop, false otherwise
 */
fun <T> hasLoop(seq: Sequence<T>): Boolean {
    try {

    } catch (e: NoSuchElementException) {
        return false
    }
    val buffer = ArrayDeque<T>(4096)
    val fastSeq = sequence<T> {
        var b = false
        seq.forEach {
            buffer.addLast(it)
            if (b) yield(it)
            b = !b
        }
    }
    val slowSeq = sequence {
        while (true) {
            yield(buffer.removeFirst())
        }
    }
    fastSeq.zip(slowSeq).forEach {
        if (it.first == it.second) return true
    }
    return false
}
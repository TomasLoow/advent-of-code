@file:Suppress("unused")
package aoc.utils.extensionFunctions

import java.io.File

fun String.nonEmptyLines(): List<String> {
    return lines().filter { it.isNotEmpty() }
}

fun File.readNonEmptyLines(): List<String> {
    return readText().nonEmptyLines()
}

fun String.ensureNl(): String {
    return replace("\r\n", "\n")
}

fun CharSequence.isAscending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a <= b }
}

fun CharSequence.isStrictlyAscending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a < b }
}

fun CharSequence.isDescending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a >= b }
}

fun CharSequence.isStrictlyDescending(): Boolean {
    return this.zipWithNext().all { (a, b) -> a > b }
}
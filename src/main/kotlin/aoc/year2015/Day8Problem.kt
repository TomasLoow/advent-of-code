package aoc.year2015

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day8Problem : DailyProblem<Int>() {

    override val number = 8
    override val year = 2015
    override val name = "Matchsticks"

    private lateinit var lines: List<String>

    override fun commonParts() {
        lines = getInputText().nonEmptyLines()
    }


    override fun part1(): Int {
        return lines.sumOf { s -> s.length - evaluate(s).length }
    }

    private fun evaluate(s: String) : String {
        val s2 = s.drop(1).dropLast(1).toMutableList()
        val cs = buildList {
            while(s2.isNotEmpty()) {
                val firstChar = s2.first()
                if (firstChar != '\\') {
                    add(firstChar)
                    s2.removeFirst()
                 } else {
                     if (s2[1] == 'x') {
                         add('X') // Actual char not important?
                         repeat(4) { s2.removeFirst() }
                     } else {
                         add(s2[1])
                         repeat(2) { s2.removeFirst() }
                     }
                }
            }
        }
        return cs.joinToString("")
    }



    private fun encode(s: String): String {
        val encoded = s.map { c ->
            when (c) {
                '\\' -> """\\"""
                '\"' -> """\""""
                else -> if (c in "0123456789abcdefghijklmnopqrstuvwxyz") {
                    c.toString()
                } else {
                    "\\$c"
                }
            }
        }.joinToString("")
        return  "\"" + encoded + "\""
    }

    override fun part2(): Int {
        lines.forEach { s ->
            encode(s)
        }
        return lines.sumOf { s ->
            encode(s).length - s.length }
    }
}

val day8Problem = Day8Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day8Problem.runBoth(1)
}
package aoc.year2021

import DailyProblem
import aoc.utils.readNonEmptyLines
import java.util.*
import kotlin.time.ExperimentalTime


class Day10Problem : DailyProblem<Long>() {

    override val number = 10
    override val year = 2021
    override val name = "Syntax Scoring"

    private lateinit var lines: List<String>
    private lateinit var scores: List<Pair<ErrorType, Long>>

    override fun commonParts() {
        lines = getInputFile().readNonEmptyLines()
        scores = lines.map {analyseLine(it)}
    }

    private fun matchingParen(openingParen: Char): Char {
        return when (openingParen) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw Exception("Bad input")
        }
    }

    private fun isOpeningParen(char: Char): Boolean {
        return char in "<([{"
    }

    private enum class ErrorType {
        MISMATCH,
        INCOMPLETE
    }

    private fun analyseLine(line: String): Pair<ErrorType, Long> {
        fun mismatchScoreOfChar(char: Char): Long {
            return when (char) {
                ')' -> 3L
                ']' -> 57L
                '}' -> 1197L
                '>' -> 25137L
                else -> throw Exception("Bad input")
            }
        }

        fun fixScoreOfChar(char: Char): Long {
            return when (char) {
                '(' -> 1L
                '[' -> 2L
                '{' -> 3L
                '<' -> 4L
                else -> throw Exception("Bad input")
            }
        }

        val stack = Stack<Char>()
        line.forEach { char ->
            if (isOpeningParen(char)) {
                stack.push(char)
            } else {
                val currentOpening = stack.pop()
                if (matchingParen(currentOpening) != char) {
                    return Pair(ErrorType.MISMATCH, mismatchScoreOfChar(char))
                }
            }
        }
        var score = 0L
        while (stack.isNotEmpty()) {
            score = 5L * score + fixScoreOfChar(stack.pop()!!)
        }
        return Pair(ErrorType.INCOMPLETE, score)
    }

    override fun part1(): Long {
        return scores.filter { it.first == ErrorType.MISMATCH }.sumOf { it.second }
    }

    override fun part2(): Long {
        val sortedScores = scores.filter { it.first == ErrorType.INCOMPLETE }.map { it.second }.sorted()
        val midpoint = (sortedScores.size - 1) / 2
        return sortedScores[midpoint]
    }
}


val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.commonParts()
    day10Problem.runBoth(10)
}


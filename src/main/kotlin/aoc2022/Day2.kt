package aoc2022

import DailyProblem
import java.io.File

typealias RPSMove = Int

const val ROCK = 0
const val PAPER = 1
const val SCISSOR = 2

typealias RPSResult = Int

const val WIN = 2
const val DRAW = 0
const val LOSE = 1

/* computer - my ≅ result (mod 3) */
/* computer - result ≅ my (mod 3) */

class Day2Problem(override val inputFilePath: String) : DailyProblem<Int> {

    override val number = 2
    override val name = "Rock Paper Scissors "

    private fun charToMove(c: String): RPSMove {
        return when (c) {
            "A" -> ROCK
            "B" -> PAPER
            "C" -> SCISSOR
            "X" -> ROCK
            "Y" -> PAPER
            "Z" -> SCISSOR
            else -> {
                throw Exception()
            }
        }
    }

    private fun charToRes(c: String): RPSResult {
        return when (c) {
            "X" -> LOSE
            "Y" -> DRAW
            "Z" -> WIN
            else -> {
                throw Exception()
            }
        }
    }

    private fun parseFilePart1(): List<Pair<RPSMove, RPSMove>> {
        return File(inputFilePath).readLines().map { line ->
            val (comp, me) = line.split(" ")
            Pair(charToMove(comp), charToMove(me))
        }
    }


    private fun parseFilePart2(): List<Pair<RPSMove, RPSResult>> {
        return File(inputFilePath).readLines().map { line ->
            val (comp, res) = line.split(" ")
            Pair(charToMove(comp), charToRes(res))
        }
    }

    override fun part1(): Int {
        return parseFilePart1().map { (comp, me) ->
            val res = (comp - me + 3) % 3
            scoreRes(res) + scoreMove(me)
        }.sum()
    }

    private fun scoreRes(res: Int): Int {
        return when (res) {
            WIN -> 6
            DRAW -> 3
            LOSE -> 0
            else -> {
                throw Exception("Bad result")
            }
        }
    }

    private fun scoreMove(move: Int): Int {
        return when (move) {
            ROCK -> 1
            PAPER -> 2
            SCISSOR -> 3
            else -> {
                throw Exception("Bad move")
            }
        }
    }


    override fun part2(): Int {
        return parseFilePart2().map { (comp, res) ->
            val me = (comp - res + 3) % 3
            scoreRes(res) + scoreMove(me)
        }.sum()
    }
}

val day2Problem = Day2Problem("input/aoc2022/day2.txt")



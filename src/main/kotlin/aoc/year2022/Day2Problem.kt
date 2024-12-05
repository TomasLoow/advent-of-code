package aoc.year2022

import DailyProblem
import aoc.utils.parseListOfPairs

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

class Day2Problem : DailyProblem<Int>() {

    override val number = 2
    override val year = 2022
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
        return parseListOfPairs(getInputText(), ::charToMove, ::charToMove)
    }


    private fun parseFilePart2(): List<Pair<RPSMove, RPSResult>> {
        return parseListOfPairs(getInputText(), ::charToMove, ::charToRes)
    }

    override fun part1(): Int {
        return parseFilePart1().sumOf { (comp, me) ->
            val res = (comp - me + 3) % 3
            scoreRes(res) + scoreMove(me)
        }
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
        return parseFilePart2().sumOf { (comp, res) ->
            val me = (comp - res + 3) % 3
            scoreRes(res) + scoreMove(me)
        }
    }
}

val day2Problem = Day2Problem()



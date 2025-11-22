package aoc.year2019

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.parseIntCodeComputer
import kotlin.math.sign
import kotlin.time.ExperimentalTime
import kotlin.toString

private data class GameState(val ballX: Int, val barX: Int, val score: Long?)

class Day13Problem : DailyProblem<Long>() {

    override val number = 13
    override val year = 2019
    override val name = "Care Package"

    private lateinit var computer: IntCode

    override fun commonParts() {
        computer = parseIntCodeComputer(getInputText())
    }

    override fun part1(): Long {
        computer.reset()
        return computer.runUntilHalt().chunked(3).count { it[2] == 2L }.toLong()
    }


    override fun part2(): Long {
        computer.reset()
        computer.memory[0] = 2L


        // Actully keeping track of the whole screen isn't *needed*, only the position of the ball and bar needs to be kept track of
        // but I don't felt like removing the code, it's more fun this way
        val screen = Array2D(44, 24, '.')

        var score = 0L
        while (true) {
            val runResult = computer.runUntilNeedsInputOrHalt()
            val gameState = runCycle(screen, runResult.output)
            if (gameState.score != null) score = gameState.score

            if (runResult.halted) {
                break
            } else {
                // Check the difference between ball and bar and move in that direction
                computer.writeInput((gameState.ballX - gameState.barX).sign.toLong())
            }
        }
        return score
    }

    private fun runCycle(screen: Array2D<Char>, output: List<Long>): GameState {
        var barX = 0
        var ballX = 0
        var score: Long? = null
        output.chunked(3).forEach { (x, y, tile) ->
            if (x == -1L && y == 0L) {
                score = tile
            } else {
                screen[x.toInt(), y.toInt()] = when (tile) {
                    0L -> '.'
                    1L -> 'X'
                    2L -> '#'
                    3L -> {
                        barX = x.toInt()
                        '='
                    }
                    4L -> {
                        ballX = x.toInt()
                        'O'
                    }
                    else -> ' '
                }
            }
        }
        //printScreen(screen)
        return GameState(ballX, barX, score)
    }

    @Suppress("unused")
    private fun printScreen(screen: Array2D<Char>) {
        screen.print { it.toString() }
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.testData = false
    day13Problem.runBoth(100)
}
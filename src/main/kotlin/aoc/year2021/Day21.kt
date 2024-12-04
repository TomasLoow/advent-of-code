package aoc.year2021

import DailyProblem
import aoc.utils.emptyMutableMap
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import java.lang.Long.max
import kotlin.time.ExperimentalTime

private fun parseInput(path: String): GameState {
    val input = File(path).readLines().map {
        it.drop("Player X starting position: ".length).toInt()
    }
    return GameState(pos1=input.first(), pos2=input.last(), score1 = 0, score2 = 0, player1sTurn = true)
}

data class GameState(
    val pos1: Int,
    val pos2: Int,
    val score1: Int,
    val score2: Int,
    val player1sTurn: Boolean
) {
    fun nextState(roll: Int): GameState {
        if (player1sTurn) {
            var newPos1 = pos1 + roll
            if (newPos1 > 10)  {
                newPos1 = (newPos1-1).mod(10) + 1
            }
            val newScore1 = score1 + newPos1
            return GameState(newPos1, pos2, newScore1, score2, false)

        } else {
            var newPos2 = pos2 + roll
            if (newPos2 > 10)  {
                newPos2 = (newPos2-1).mod(10) + 1
            }
            val newScore2 = score2 + newPos2
            return GameState(pos1, newPos2, score1, newScore2, true)
        }
    }
}

fun runDeterministicGame(startState: GameState): Int{
    var dice = 0
    var state = startState
    while (max(state.score1, state.score2) < 1000) {
        for (playerIdx in (0..1)) {
            val tripleRoll = (dice+2)*3
            state = state.nextState(tripleRoll)
            dice += 3

            if (max(state.score1, state.score2) >= 1000) break
        }
    }

    return dice * min(state.score1, state.score2)
}

fun runDiracGame(startState: GameState): Pair<Long,Long>{
    val memoized = emptyMutableMap<GameState, Pair<Long,Long>>()
    val threeDiceSumOutcomes = listOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1) // Pair(roll sum, count rolls with sum)
    fun rec(state: GameState) : Pair<Long,Long> {
        if (state.score1 >= 21) return Pair(1,0)
        if (state.score2 >= 21) return Pair(0,1)

        val outComes: List<Pair<Int, Pair<Long, Long>>> =
            threeDiceSumOutcomes.map { it: Pair<Int, Int> ->
                val (roll, count) =  it
                val newState = state.nextState(roll)
                if (newState !in memoized) {
                    memoized[newState]  =  rec(newState)
                }
                Pair(count, memoized[newState]!!)
            }
        val p1Wins = outComes.sumOf { it.first* it.second.first}
        val p2Wins = outComes.sumOf { it.first* it.second.second}
        return Pair(p1Wins,p2Wins)
    }
    return rec(startState)
}



class Day21Problem() : DailyProblem<Long>() {
    override val number = 21
    override val year = 2021
    override val name = "Dirac Dice"

    override fun part1(): Long {
        val input = parseInput(getInputFile().absolutePath)
        return runDeterministicGame(input).toLong()
    }

    override fun part2(): Long {
        val input = parseInput(getInputFile().absolutePath)
        val res = runDiracGame(input)

        return max(res.first, res.second)
    }
}

val day21Problem = Day21Problem()


@OptIn(ExperimentalTime::class)
fun main() {
    day21Problem.runBoth(100)
}
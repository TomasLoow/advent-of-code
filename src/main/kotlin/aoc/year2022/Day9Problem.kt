package aoc.year2022

import DailyProblem
import aoc.utils.Coord
import aoc.utils.Direction
import aoc.utils.iterate
import aoc.utils.parseListOfPairs
import java.lang.Integer.parseInt
import kotlin.math.sign
import kotlin.time.ExperimentalTime

class Day9Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2022
    override val name = "Rope Bridge"

    private lateinit var moves: List<Pair<Direction, Int>>


    override fun commonParts() {
        fun parseDir(d: String) = when (d) {
            "D" -> Direction.DOWN
            "U" -> Direction.UP
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> {
                throw Exception("Bad input")
            }
        }
        moves = parseListOfPairs(getInputText(), ::parseDir, ::parseInt)
    }


    private fun findHeadPath(): MutableList<Coord> {
        val headPositions = mutableListOf<Coord>()
        var headPos = Coord.origin
        moves.forEach { move ->
            val (dir, len) = move
            repeat(len) {
                headPos = headPos.stepInDir(dir)
                headPositions.add(headPos)
            }
        }
        return headPositions
    }

    private fun followCoord(follower: Coord, leader: Coord): Coord {
        val (dx, dy) = leader - follower
        if (leader.chebyshevDistanceTo(follower) < 2) return follower

        return follower + Pair(dx.sign, dy.sign)
    }

    private fun followRopeBehindPath(
        leaderPath: List<Coord>
    ): List<Coord> {
        val tailPositions = leaderPath.runningFold(Coord.origin) { currentPos, head ->
            followCoord(currentPos, head)
        }
        return tailPositions
    }

    override fun part1(): Long {
        val headPositions = findHeadPath()
        val tailPositions = followRopeBehindPath(headPositions)
        return tailPositions.distinct().size.toLong()
    }

    override fun part2(): Long {
        val headPositions = findHeadPath()
        val tailTrail = ::followRopeBehindPath.iterate(headPositions, 9)
        return tailTrail.distinct().size.toLong()
    }
}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.runBoth(1000)
}
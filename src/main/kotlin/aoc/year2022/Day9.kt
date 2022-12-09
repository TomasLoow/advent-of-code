package aoc.year2022

import DailyProblem
import aoc.utils.*
import java.lang.Math.abs
import kotlin.math.sign
import kotlin.time.ExperimentalTime

class Day9Problem() : DailyProblem<Long>() {

    override val number = 9
    override val year = 2022
    override val name = "Rope Bridge"

    private lateinit var moves: List<Pair<Direction,Int>>

    private fun parseFile(): List<Pair<Direction,Int>> {
        return getInputText().nonEmptyLines().map { line ->
            val (d, l) = line.split(" ")
            val dir = when (d) {
                "D" -> Direction.DOWN
                "U" -> Direction.UP
                "L" -> Direction.LEFT
                "R" -> Direction.RIGHT
                else -> { throw Exception("Bad input")}
            }
            Pair(dir, l.toInt())
        }
    }

    override fun commonParts() {
        moves = parseFile()
    }


    private fun findHeadPath(): MutableList<Coord> {
        val headPositions= mutableListOf<Coord>()
        var headPos = Coord(0, 0)
        moves.forEach { move ->
            val (dir, len) = move
            repeat(len) {
                headPos = headPos.stepInDir(dir)
                headPositions.add(headPos)
            }
        }
        return headPositions
    }

    private fun followCoord(follower: Coord, leader: Coord) : Coord{
        val dx = leader.first-follower.first
        val dy = leader.second-follower.second
        if (abs(dx) < 2 && abs(dy) < 2) {
            return follower
        }

        return Coord(follower.first + dx.sign, follower.second + dy.sign)
    }

    private fun followRopeBehindPath(
        leaderPath: List<Coord>,
        startPos: Coord
    ): List<Coord> {
        val tailPositions = leaderPath.runningFold(startPos) { currentPos, head ->
            followCoord(currentPos, head)
        }
        return tailPositions
    }

    override fun part1(): Long {
        val headPositions = findHeadPath()
        val tailPositions = followRopeBehindPath(headPositions, Coord(0,0))
        return tailPositions.toSet().size.toLong()
    }

    override fun part2(): Long {
        val headPositions = findHeadPath()
        var tailTrail = headPositions.toList()
        repeat(9) {
            tailTrail = followRopeBehindPath(tailTrail, Coord(0, 0))
        }
        return tailTrail.toSet().size.toLong()
    }}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.runBoth(1000)
}
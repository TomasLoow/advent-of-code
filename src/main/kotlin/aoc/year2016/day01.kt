 package aoc.year2016

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.parseDirectionFromURDL
import kotlin.time.ExperimentalTime

 class Day01Problem : DailyProblem<Int>() {

    override val number = 1
    override val year = 2016
    override val name = "No Time for a Taxicab"

    private lateinit var moves: List<Pair<Direction, Int>>

    override fun commonParts() {
        moves = getInputText().nonEmptyLines().first().split(", ").map { instr ->
            Pair(parseDirectionFromURDL(instr[0]), instr.drop(1).toInt())
        }
    }

    override fun part1(): Int {
        var pos = Coord.origin
        var dir = Direction.UP
        moves.forEach { (d, steps) ->
            dir = when(d) {
                Direction.RIGHT -> dir.rotateCW()
                Direction.LEFT -> dir.rotateCCW()
                else -> throw Exception("Bad direction")
            }
            pos = pos.stepInDir(dir, steps)
        }
        return pos.manhattanDistanceTo(Coord.origin)
    }

    override fun part2(): Int {
        var pos = Coord.origin
        var dir = Direction.UP
        val visited = mutableSetOf(pos)
        moves.forEach { (d, steps) ->
            dir = when(d) {
                Direction.RIGHT -> dir.rotateCW()
                Direction.LEFT -> dir.rotateCCW()
                else -> throw Exception("Bad direction")
            }
            repeat(steps) {
                pos = pos.stepInDir(dir)
                if (pos in visited) return pos.manhattanDistanceTo(Coord.origin)
                visited.add(pos)
            }
        }
        throw Exception("Should never happen, no answer found")
    }
}

val day01Problem = Day01Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day01Problem.testData = false
    day01Problem.runBoth(1)
}
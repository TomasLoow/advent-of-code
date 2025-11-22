package aoc.year2019

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import kotlin.time.ExperimentalTime

private typealias WireSpec = List<Pair<Direction, Int>>

class Day03Problem : DailyProblem<Int>() {

    override val number = 3
    override val year = 2019
    override val name = "Crossed Wires"

    private lateinit var wires: List<WireSpec>
    private lateinit var coords1: List<Coord>
    private lateinit var coords2: List<Coord>
    private lateinit var intersections: Set<Coord>

    override fun commonParts() {
        wires = getInputText().nonEmptyLines().map { line ->
            line.split(",").map { step ->
                when (step.first()) {
                    'U' -> Pair(Direction.UP, step.drop(1).toInt())
                    'D' -> Pair(Direction.DOWN, step.drop(1).toInt())
                    'R' -> Pair(Direction.RIGHT, step.drop(1).toInt())
                    'L' -> Pair(Direction.LEFT, step.drop(1).toInt())
                    else -> throw Exception("bad input")
                }
            }
        }
        coords1 = materialize(wires[0])
        coords2 = materialize(wires[1])
        intersections = coords1.toSet().intersect(coords2.toSet())

    }

    override fun part1(): Int {
        return intersections.minOf { Coord.origin.manhattanDistanceTo(it)}
    }

    private fun materialize(wireSpec: List<Pair<Direction, Int>>): List<Coord> {
        var c = Coord(0, 0)
        return buildList {
            wireSpec.forEach { (dir, steps) ->
                val path = c.walkInDir(dir).drop(1).take(steps).toList()
                c = path.last()
                addAll(path)
            }
        }
    }

    override fun part2(): Int {
        return intersections.minOf { c ->
            coords1.indexOf(c)+1 + coords2.indexOf(c)+1
        }
    }
}

val day03Problem = Day03Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day03Problem.testData = false
    day03Problem.runBoth(100)
}
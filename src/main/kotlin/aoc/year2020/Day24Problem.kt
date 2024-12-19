package aoc.year2020

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day24Problem : DailyProblem<Int>() {

    override val number = 24
    override val year = 2020
    override val name = "Lobby Layout"

    private lateinit var data: List<List<Direction>>
    private lateinit var initialFlippedAfterPart1: MutableSet<Coord>

    override fun commonParts() {
        fun parseLine(s: String): List<Direction> {
            if (s.isEmpty()) return emptyList()
            val d = when (s.first()) {
                'e' -> Direction.RIGHT
                'w' -> Direction.LEFT
                's' -> {
                    if (s[1] == 'e') Direction.DOWN else Direction.DOWNLEFT
                }

                'n' -> {
                    if (s[1] == 'e') Direction.UPRIGHT else Direction.UP
                }

                else -> TODO("invalid direction")
            }
            if (s.first() in "ns") {
                return listOf(d) + parseLine(s.drop(2))
            } else {
                return listOf(d) + parseLine(s.drop(1))
            }
        }
        data = getInputText().nonEmptyLines().map(::parseLine)
        followPart1Moves()

    }

    private fun followPart1Moves() {
        initialFlippedAfterPart1 = mutableSetOf<Coord>()
        var coord = Coord.origin
        data.forEach { line ->
            coord = Coord.origin
            line.forEach { d ->
                coord = coord + d
            }
            if (coord in initialFlippedAfterPart1) {
                initialFlippedAfterPart1.remove(coord)
            } else {
                initialFlippedAfterPart1.add(coord)
            }
        }
    }

    override fun part1(): Int {
        return initialFlippedAfterPart1.size
    }

    fun Coord.hexNeighbors(): List<Coord> = listOf(
        this.copy(x = this.x - 1),
        this.copy(x = this.x + 1),
        this.copy(y = this.y - 1),
        this.copy(y = this.y + 1),
        this.copy(x = this.x + 1, y = this.y - 1),
        this.copy(x = this.x - 1, y = this.y + 1),
    )

    fun evolve(map: Set<Coord>): Set<Coord> {
        val whiteNeighbors = map.flatMap { it.hexNeighbors() }.filter { it !in map }
        val whiteToFlip = whiteNeighbors.filter { white ->
            white.hexNeighbors().count { it in map } == 2
        }
        val blackToFlip = map.filter { it.hexNeighbors().count { n -> n in map } !in (1..2) }
        return map - blackToFlip + whiteToFlip
    }

    override fun part2(): Int {
        var map = initialFlippedAfterPart1.toSet()
        (1..100).forEach { idx ->
            map = evolve(map)
        }
        return map.size
    }
}

val day24Problem = Day24Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day24Problem.testData = false
    day24Problem.runBoth(10)
}
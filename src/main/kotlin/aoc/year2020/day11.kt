package aoc.year2020

import aoc.DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import kotlin.time.ExperimentalTime

enum class Seat {
    FLOOR, FREE, TAKEN;
}

class Day11Problem : DailyProblem<Int>() {

    override val number = 11
    override val year = 2020
    override val name = "Seating System"

    private lateinit var initialFloor: Array2D<Seat>
    private lateinit var cachedAdjacent: MutableMap<Coord, List<Coord>>
    private lateinit var cachedSeen: MutableMap<Coord, List<Coord>>

    override fun commonParts() {
        initialFloor = Array2D.parseFromLines(getInputText()) { c ->
            when (c) {
                'L' -> Seat.FREE
                '.' -> Seat.FLOOR
                else -> throw Exception("Unknown character $c")
            }
        }
        cachedAdjacent = mutableMapOf()
        cachedSeen = mutableMapOf()
    }

    fun stepPart1(a: Array2D<Seat>, intoArray: Array2D<Seat>) {
        a.mapIndexedInto(intoArray) { coord, seat ->
            when (seat) {
                Seat.FLOOR -> Seat.FLOOR
                Seat.FREE -> {
                    val adjacent: Int = adjacentSeats(coord, a).count { a[it] == Seat.TAKEN }
                    if (adjacent == 0) Seat.TAKEN
                    else Seat.FREE
                }

                Seat.TAKEN -> {
                    val adjacent: Int = adjacentSeats(coord, a).count { a[it] == Seat.TAKEN }
                    if (adjacent >= 4) Seat.FREE
                    else Seat.TAKEN
                }
            }
        }    }

    fun stepPart2(a: Array2D<Seat>, intoArray: Array2D<Seat>) {
        a.mapIndexedInto(intoArray) { coord, seat ->
            when (seat) {
                Seat.FLOOR -> Seat.FLOOR
                Seat.FREE -> {
                    val seen: Int = seenSeats(coord, a).count { a[it] == Seat.TAKEN }
                    if (seen == 0) Seat.TAKEN
                    else Seat.FREE
                }

                Seat.TAKEN -> {
                    val seen: Int = seenSeats(coord, a).count { a[it] == Seat.TAKEN }

                    if (seen >= 5) Seat.FREE
                    else Seat.TAKEN
                }
            }
        }
    }

    private fun adjacentSeats(c: Coord, a: Array2D<Seat>): List<Coord> {
        if (c in cachedAdjacent) return cachedAdjacent[c]!!
        val ns = a.neighbourCoords(c, diagonal = true).filter { it in a }
        cachedAdjacent[c] = ns
        return ns
    }

    private fun seenSeats(c: Coord, a: Array2D<Seat>): List<Coord> {
        if (c in cachedSeen) return cachedSeen[c]!!
        val seen = Direction.entries.mapNotNull { dir ->
            c.walkInDir(dir).drop(1).takeWhile { it in a }.dropWhile { a[it] == Seat.FLOOR }.firstOrNull()
        }
        cachedSeen[c] = seen
        return seen
    }

    @Suppress("unused")
    fun printMap(a: Array2D<Seat>) {
        a.print { s ->
            when (s) {
                Seat.FLOOR -> "."
                Seat.FREE -> "L"
                Seat.TAKEN -> "#"
            }
        }
    }

    override fun part1(): Int {
        cachedAdjacent.clear()
        var a = initialFloor
        var a2 = Array2D(a.width, a.height, Seat.FLOOR)
        while (true) {
            stepPart1(a, a2)
            if (a == a2) {
                return (a.count { it == Seat.TAKEN })
            }
            a = a2.also { a2 = a }  // Switcheroo!
        }
    }


    override fun part2(): Int {
        cachedSeen.clear()
        var a = initialFloor
        var a2 = Array2D(a.width, a.height, Seat.FLOOR)
        while (true) {
            stepPart2(a, a2)
            if (a == a2) {
                return (a.count { it == Seat.TAKEN })
            }
            a = a2.also { a2 = a } // Swaparoo!
        }
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.testData = false
    day11Problem.runBoth(10)
}
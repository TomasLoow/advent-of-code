package aoc.year2024

import DailyProblem
import aoc.utils.*
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import kotlin.time.ExperimentalTime

class Day10Problem : DailyProblem<Int>() {

    override val number = 10
    override val year = 2024
    override val name = "Hoof It"

    private lateinit var map: Array2D<Int>
    private lateinit var zeros: List<Coord>

    override fun commonParts() {
        map = parseIntArray(getInputText())
        zeros = map.mapAndFilterToListByNotNull { c, v -> if (v == 0) c else null }

    }


    private fun solve(z: Coord): List<Coord> {
        val q = mutableListOf(z to 0)
        var done = false
        while (!done) {
            val (pos, dist) = q.first()
            if (dist == 9) {
                break
            } else {
                for (n in map.neighbourCoordsAndValues(pos, diagonal = false)) {
                    if (n.value == dist + 1) q.add(n.toPair())
                }
            }
            q.removeFirst()
        }
        return q.map { it.first }.toList()
    }

    private fun solve1(z: Coord): Int {
        return solve(z).distinct().size
    }

    private fun solve2(z: Coord): Int {
        return solve(z).size
    }


    override fun part1(): Int {
        return zeros.sumOf {
            try {
                solve1(it)
            } catch (e: BFSNoPathFound) {
                0
            }
        }
    }

    override fun part2(): Int {
        return zeros.sumOf {
            try {
                solve2(it)
            } catch (e: BFSNoPathFound) {
                0
            }
        }
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.testData = false
    day10Problem.runBoth(100)
}
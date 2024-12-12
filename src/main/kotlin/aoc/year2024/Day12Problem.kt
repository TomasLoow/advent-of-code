package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

/**
 * Represents a border of a region. Consists of two coord, where one is inside the region and the other is outside the region.
 */
private data class Fence(val inside: Coord, val outside: Coord) {

    /**
     * Aa and Bb are adjacent...
     *
     * here         but not here
     * Aa           Aa
     * Bb           bB
     */
    fun adjacent(other: Fence): Boolean {
        return inside.isNeighbourWith(other.inside) && outside.isNeighbourWith((other.outside))
    }
}

private typealias Side = Set<Fence>  // A set of fence pieces where each piece is adjacent to some other in the set

class Day12Problem : DailyProblem<Int>() {

    override val number = 12
    override val year = 2024
    override val name = "Garden Groups"

    private lateinit var map: Array2D<Char>
    private lateinit var regions: List<Pair<Char, Set<Coord>>>
    override fun commonParts() {
        map = parseCharArray(getInputText())
        regions = buildList {
            val handled = mutableSetOf<Coord>()
            map.forEach { coord, v ->
                if (coord !in handled) {
                    val region = map.floodFill(coord)
                    handled.addAll(region)
                    add(v to region.toMutableSet())
                }
            }
        }
    }

    fun countPerimeter(set: Set<Coord>): Int {
        return set.sumOf { coord ->
            4 - coord.neighbours().count { it in set }
        }
    }

    private fun countSides(set: Set<Coord>): Int {
        val neighbourCoord = set.flatMap { it.neighbours() }.toSet() - set
        val sides = mutableSetOf<MutableSet<Fence>>()  // pairs of in-set to outside-set
        set.forEach { c ->
            c.neighbours().forEach { n -> if (n in neighbourCoord) sides.add(mutableSetOf(Fence(c, n))) }
        }
        // sides now contains only singleton-sets next try to merge them with each other
        var merges = Int.MAX_VALUE
        while (merges > 0) {
            merges = 0
            for ((side1, side2) in sides.toList().allUnorderedPairs()) {
                if (canMerge(side1, side2)) {
                    sides.remove(side2)
                    side1.addAll(side2)
                    merges++
                }
            }
        }
        return sides.size
    }

    private fun canMerge(side1: Side, side2: Side): Boolean {
        side1.forEach { e1 ->
            if (side2.any { e2 -> e1.adjacent(e2) }) return true
        }
        return false
    }


    override fun part1(): Int {
        return regions.sumOf { (_, set) ->
            countPerimeter(set) * set.size
        }
    }


    override fun part2(): Int {
        return regions.asSequence().sumOf { (_, set) ->
            countSides(set) * set.size
        }
    }
}

val day12Problem = Day12Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day12Problem.testData = false
    day12Problem.runBoth(5)
}
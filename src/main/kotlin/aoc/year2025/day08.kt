package aoc.year2025

import aoc.DailyProblem
import aoc.utils.parseListOfTriples
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime

typealias Coord3D = Triple<Int, Int, Int>

var t = Coord3D(1, 2, 3)

fun sq(a: Int) = (a.toLong() * a.toLong())
fun Coord3D.dist(other: Coord3D): Long = sq(first - other.first) + sq(second - other.second) + sq(third - other.third)

class Day08Problem : DailyProblem<Long>() {

    override val number = 8
    override val year = 2025
    override val name = "Playground"

    private lateinit var boxes: Array<Coord3D>
    private lateinit var distances: List<Triple<Int, Int, Long>>
    private var numToTakePt1 = 1000
    override fun commonParts() {
        boxes = parseListOfTriples(
            getInputText(),
            ::parseInt,
            ::parseInt,
            ::parseInt,
            separator1 = ",",
            separator2 = ","
        ).toTypedArray()

        val distList = boxes.indices.flatMap { i ->
            ((i + 1)..boxes.lastIndex).map { j ->
                Triple(i, j, boxes[i].dist(boxes[j]))
            }
        }
        // Heuristic, assume we can ignore every link that's 1/10 of the longest distance between any boxes
        // Gives a good speedup and seems to work
        val mx = distList.maxOf { it.third }
        val cutoff = if (testData) mx/4 else mx / 100
        distances = distList.filter { it.third < cutoff }.sortedBy { it.third }
        if (testData) numToTakePt1 = 10
    }

    override fun part1(): Long {
        // Give a unique group id to each box
        var idx = 0
        var groupings = Array(boxes.size) {
            idx++
            idx - 1
        }

        val nClosest = distances.take(numToTakePt1)
        nClosest.forEach { (i, j, _) -> group(i, j, groupings) }
        val groupSizes = groupings.groupingBy { it }.eachCount().values.sortedDescending()
        return groupSizes.take(3).reduce { a, b -> a * b }.toLong()
    }

    private fun group(b1: Int, b2: Int, groupings: Array<Int>) {
        require(b1 != b2)
        val group1 = groupings[b1]
        val group2 = groupings[b2]
        groupings.indices.forEach { i ->
            if ((groupings[i] == group2)) groupings[i] = group1
        }
    }

    private fun allGrouped(groupings: Array<Int>): Boolean {
        val g = groupings[0]
        return groupings.all { it == g }
    }

    override fun part2(): Long {
        // Give a unique group id to each box
        var idx = 0
        var groupings = Array(boxes.size) {
            idx++
            idx - 1
        }

        var lastAdded: Pair<Int, Int>? = null
        for ((i, j, _) in distances) {
            group(i, j, groupings)
            if (allGrouped(groupings)) {
                lastAdded = Pair(i, j)
                break
            }
        }

        val firstX = boxes[lastAdded!!.first].first.toLong()
        val secondX = boxes[lastAdded!!.second].first.toLong()
        return firstX * secondX
    }
}

val day08Problem = Day08Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day08Problem.testData = true
    day08Problem.runBoth(1)
}
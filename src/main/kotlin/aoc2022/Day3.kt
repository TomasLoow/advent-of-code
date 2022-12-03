package aoc2022

import DailyProblem
import java.io.File


class Day3Problem(override val inputFilePath: String) : DailyProblem {

    override val number = 3
    override val name = "Rucksack Reorganization"

    private fun parseFile(): List<Pair<String, String>> {
        return File(inputFilePath)
            .readLines()
            .filter { it.isNotEmpty() }
            .map { line ->
                val half = line.length / 2
                Pair(line.take(half), line.drop(half))
            }
    }

    private fun parseFile2(): List<List<String>> {
        return File(inputFilePath).readLines().chunked(3)
    }

    override fun part1(): Long {
        return parseFile().sumOf { (first, second) ->
            score(first.find { c -> c in second }!!)
        }.toLong()
    }

    private fun score(it: Char): Int {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(it) + 1
        // Don't laugh. It works.
    }

    override fun part2(): Long {
        return parseFile2().sumOf { group ->
            group.map { it.toSet() }// Turn each line to a set
                .reduce { a, b -> a.intersect(b) } // Find the intersection
                .sumOf { score(it) } // "Sum" the scores, but it really should be only one element here
        }.toLong()
    }
}

val day3Problem = Day3Problem("input/aoc2022/day3.txt")

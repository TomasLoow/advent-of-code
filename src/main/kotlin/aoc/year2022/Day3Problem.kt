package aoc.year2022

import DailyProblem
import aoc.utils.nonEmptyLines


class Day3Problem : DailyProblem<Int>() {

    override val number = 3
    override val year = 2022
    override val name = "Rucksack Reorganization"

    private fun parseFile(): List<Pair<String, String>> {
        return getInputFile()
            .readText()
            .nonEmptyLines()
            .map { line ->
                val half = line.length / 2
                Pair(line.take(half), line.drop(half))
            }
    }

    private fun parseFile2(): List<List<String>> {
        return getInputFile().readLines().chunked(3)
    }

    private fun score(it: Char): Int {
        return "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(it) + 1
        // Don't laugh. It works.
    }

    override fun part1(): Int {
        return parseFile().sumOf { (first, second) ->
            score(first.find { c -> c in second }!!)
        }
    }

    override fun part2(): Int {
        return parseFile2().sumOf { group ->
            group.map { it.toSet() }// Turn each line to a set
                .reduce { a, b -> a.intersect(b) } // Find the intersection
                .sumOf { score(it) } // "Sum" the scores, but it really should be only one element here
        }
    }
}

val day3Problem = Day3Problem()

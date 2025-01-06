package aoc.year2021

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.parseDecimalList
import aoc.utils.parseListOfPairs
import aoc.utils.parseOneLineOfSeparated

typealias ActiveWires = Set<Char>

class Day08Problem : DailyProblem<Long>() {
    override val number = 8
    override val year = 2021
    override val name = "Seven Segment Search"

    private lateinit var solved: List<List<Int>>

    private fun parseDisplaysFile(): List<Pair<List<ActiveWires>, List<ActiveWires>>> {
        fun strToWires(data: String): ActiveWires = data.toCharArray().toSet()
        fun parseWiresList(s: String): List<ActiveWires> = parseOneLineOfSeparated(s, ::strToWires, " ")
        return parseListOfPairs(getInputText(), ::parseWiresList, ::parseWiresList, separator = " | ")
    }

    override fun commonParts() {
        val lines = parseDisplaysFile()
        solved = lines.map { solveLine(it) }
    }

    private fun solveLine(line: Pair<List<ActiveWires>, List<ActiveWires>>): List<Int> {
        val (clues, displays) = line

        val numToWiresMap: MutableMap<Int, ActiveWires> = emptyMutableMap()
        numToWiresMap[1] = clues.find { it.size == 2 }!!
        numToWiresMap[4] = clues.find { it.size == 4 }!!
        numToWiresMap[7] = clues.find { it.size == 3 }!!
        numToWiresMap[8] = clues.find { it.size == 7 }!!

        // Determine the numbers with six segments lit (0, 6 and 9)
        val sixSegments = clues.filter { it.size == 6 }

        for (sixClue in sixSegments) {
            if (sixClue.containsAll(numToWiresMap[4]!!)) // Only the six fully contains the four
                numToWiresMap[9] = sixClue
            else if (sixClue.containsAll(numToWiresMap[1]!!)) // Only the zero fully contains the one
                numToWiresMap[0] = sixClue
            else
                numToWiresMap[6] = sixClue
        }

        // Determine the numbers with five segments lit (2,3 and 5)
        val fiveSegments = clues.filter { it.size == 5 }

        for (fiveClue in fiveSegments) {
            if (fiveClue.containsAll(numToWiresMap[1]!!)) // Only the three fully contains the one
                numToWiresMap[3] = fiveClue
            else if (numToWiresMap[6]!!.containsAll(fiveClue)) // Only the five is contained in the six
                numToWiresMap[5] = fiveClue
            else
                numToWiresMap[2] = fiveClue
        }
        // We now know all digits. Reverse the map and parse the entries from the RHS
        val reversed: Map<ActiveWires, Int> = numToWiresMap.entries.associate { entry -> Pair(entry.value, entry.key) }
        return displays.map { reversed[it]!! }
    }


    override fun part1(): Long {
        val interestingDigits = setOf(1, 4, 7, 8)
        return solved.sumOf { digits ->
            digits.count { digit -> interestingDigits.contains(digit) }
        }.toLong()
    }

    override fun part2(): Long {
        val converted = solved.map { digits -> digits.parseDecimalList() }
        return converted.sum().toLong()
    }
}

val day08Problem = Day08Problem()
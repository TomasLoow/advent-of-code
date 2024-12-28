package aoc.year2021

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.toInt

typealias BinaryArray = Array<Boolean>

class Day3Problem : DailyProblem<Long>() {
    override val number = 3
    override val year = 2021
    override val name = "Binary Diagnostic"

    private lateinit var input: List<BinaryArray>

    private enum class CountMode {
        MostCommon,
        LeastCommon,
    }

    private fun parseBinaryFile(): List<BinaryArray> {
        val lines: List<String> = getInputText().nonEmptyLines()
        return lines.map { line ->
            line.map { c -> c == '1' }.toTypedArray()
        }
    }

    private fun findMostCommonByIndex(rows: List<BinaryArray>, idx: Int, mode: CountMode): Boolean {
        var countTrue = 0
        var countFalse = 0
        for (row in rows) {
            if (row[idx]) {
                countTrue++
            } else {
                countFalse++
            }
        }
        if (mode == CountMode.MostCommon) {
            return (countTrue >= countFalse)
        }
        return (countTrue < countFalse)
    }

    private fun bitCriteria(mode: CountMode): Int {
        var data = input.toList() // Make a copy
        val rowLength: Int = data[0].size

        for (idx in 0 until rowLength) {
            val target = findMostCommonByIndex(data, idx, mode)
            data = data.filter { it[idx] == target }
            if (data.size == 1) break
        }
        return data[0].toInt()
    }

    override fun commonParts() {
        input = parseBinaryFile()
    }

    override fun part1(): Long {

        val rowLength: Int = input[0].size

        val delta: BinaryArray = BinaryArray(rowLength) { false }
        val epsilon: BinaryArray = BinaryArray(rowLength) { false }

        for (idx in 0 until rowLength) {
            val mostCommon = findMostCommonByIndex(input, idx, CountMode.MostCommon)
            delta[idx] = mostCommon
            epsilon[idx] = !mostCommon
        }

        val deltaVal = delta.toInt()
        val epsilonVal = epsilon.toInt()

        return (deltaVal * epsilonVal).toLong()
    }

    override fun part2(): Long {
        val oxygen = bitCriteria(CountMode.MostCommon)
        val co2 = bitCriteria(CountMode.LeastCommon)
        return (oxygen * co2).toLong()
    }
}


val day3Problem = Day3Problem()

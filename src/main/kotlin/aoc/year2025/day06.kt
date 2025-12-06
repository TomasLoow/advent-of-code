package aoc.year2025

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.extensionFunctions.mirrorDiagonally
import aoc.utils.sum
import java.math.BigInteger
import kotlin.time.ExperimentalTime
import kotlin.toBigInteger

enum class Op { Add, Mult }

typealias NumStringWithPadding = String

class Day06Problem : DailyProblem<BigInteger>() {

    override val number = 6
    override val year = 2025
    override val name = "Trash Compactor"

    private lateinit var columns: Array<List<NumStringWithPadding>>
    private lateinit var operators: Array<Op>

    override fun commonParts() {
        var lines = getInputText().nonEmptyLines()

        // Parse operators and save their positions in the input
        val lastLine = lines.last()
        val numColumns = lastLine.count { it != ' ' }
        val operatorPositions = IntArray(numColumns)
        operators = Array(numColumns) { Op.Add }
        var operatorIdx = 0
        lastLine.withIndex().forEach { (positionInInput, char) ->
            if (char == '*') {
                operatorPositions[operatorIdx] = positionInInput
                operators[operatorIdx] = Op.Mult
                operatorIdx++
            } else if (char == '+') {
                operatorPositions[operatorIdx] = positionInInput
                operators[operatorIdx] = Op.Add
                operatorIdx++
            }
        }
        val rows: List<List<String>> = lines
            .dropLast(1)
            .map { line ->
                operatorPositions.mapIndexed { i, p ->
                    val width = if (i == operatorPositions.lastIndex) {
                        (line.length - p)
                    } else {
                        operatorPositions[i + 1] - p - 1
                    }
                    line.substring(p, p + width)
                }
            }
        columns = rows.mirrorDiagonally().toTypedArray()
    }

    override fun part1(): BigInteger {
        val colVals = operators.indices.map { i ->
            val operator = operators[i]
            val nums = columns[i].map { it.trim().toBigInteger() }
            if (operator == Op.Add) nums.sum() else nums.reduce { a, b -> a * b }
        }
        return colVals.sum()
    }

    override fun part2(): BigInteger {
        val colVals = operators.indices.map { i ->
            val op = operators[i]
            val nums = columns[i]
            if (op == Op.Add) cephalodSum(nums) else cephalodMult(nums)
        }
        return colVals.sum()
    }

    private fun cephalodSum(nums: List<NumStringWithPadding>): BigInteger {
        val columnWidth = nums.maxOfOrNull { it.length } ?: 0
        var res = 0.toBigInteger()
        for (i in 0..<columnWidth) {
            res += nums.map { row -> row[i] }.filter { it != ' ' }.joinToString("").toBigInteger()
        }
        return res
    }

    private fun cephalodMult(nums: List<NumStringWithPadding>): BigInteger {
        val columnWidth = nums.maxOfOrNull { it.length } ?: 0
        var res = 1.toBigInteger()
        for (i in 0..<columnWidth) {
            res *= nums.map { row -> row[i] }.filter { it != ' ' }.joinToString("").toBigInteger()
        }
        return res
    }
}

val day06Problem = Day06Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day06Problem.testData = true
    day06Problem.runBoth(100)
}
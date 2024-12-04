package aoc.year2021

import DailyProblem
import aoc.utils.Axis2D
import aoc.utils.nonEmptyLines
import aoc.utils.parseDisplay
import aoc.utils.parseTwoBlocks
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime


private data class Point(var x: Int, var y: Int)  // Use dataclass instead of Pair so it's mutable


private data class Fold(val direction: Axis2D, val foldLinePos:Int) {
    fun applyToGrid(grid: Array<Point>) {
        when (direction) {
            Axis2D.Y -> grid
                .forEach { p ->
                            val y = p.y
                            if (y > foldLinePos) {
                                p.y = 2 * foldLinePos - y
                            }
                        }
            Axis2D.X -> grid
                .forEach { p ->
                            val x= p.x
                            if (x > foldLinePos) {
                                p.x = 2 * foldLinePos - x
                            }
                        }

        }
    }
}


class Day13Problem : DailyProblem<Any>() {

    override val number = 13
    override val year = 2021
    override val name = "Transparent Origami"

    lateinit var output : String  // We will put the answer to part 2 here

    private lateinit var parseResult: Pair<Array<Point>, List<Fold>>


    private fun parseFoldingFile(): Pair<Array<Point>, List<Fold>> {
        fun parseDots(s: String): Array<Point> {
            return s.lines()
                .map { line ->
                    val (x, y) = line.split(",").map(::parseInt)
                    Point(x, y)
                }.toTypedArray()
        }

        fun parseFolds(s: String): List<Fold> {
            return s.nonEmptyLines().map { line ->
                val (axis, index) = line.removePrefix("fold along ").split("=")
                Fold(if (axis == "x") Axis2D.X else Axis2D.Y, index.toInt())
            }
        }
        return parseTwoBlocks(getInputText(), ::parseDots, ::parseFolds)
    }

    private fun showGrid(pairCollection: Array<Point>): String {
        val maxX = pairCollection.maxOf { it.x }+1
        val maxY = pairCollection.maxOf { it.y }

        return (0..maxY).joinToString("\n") { y ->
            (0..maxX).map { x ->
                if (Point(x, y) in pairCollection) '█' else ' '
            }.joinToString("")
        }
    }


    override fun commonParts() {
        parseResult = parseFoldingFile()
    }
    override fun part1(): Int {
        val (grid, folds) = parseResult
        val firstFold = folds.first()

        firstFold.applyToGrid(grid)
        return grid.toSet().size
    }

    override fun part2(): String {
        val (grid, folds) = parseResult

        folds.forEach { fold ->
                fold.applyToGrid(grid)
        }
        output = showGrid(grid)
        return parseDisplay(output)  // Not an integer solution. Answers stored in display attribute instead
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.commonParts()
    day13Problem.runBoth(1)
}

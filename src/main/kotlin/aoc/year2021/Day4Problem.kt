package aoc.year2021


import DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.parseBlockList
import java.io.File
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime

private fun parseBingoFile(path: String): Pair<List<Int>, List<BingoBoard>> {
    var lines: List<String> = File(path).readLines()
    val balls = lines[0]
        .split(",")
        .map(::parseInt)
    lines = lines.drop(2)

    val boards: List<BingoBoard> = parseBlockList(lines.joinToString("\n")) { BingoBoard(it) }
    return Pair(balls, boards)
}

private class BingoBoard() {
    constructor(s: String) : this() {
        val rows = s.lines()
        numbers = Array2D(rows.flatMap { row -> row
            .trim()
            .split(" +".toRegex())
            .map { number -> number.toInt() }
        }, 5, 5)
    }
    private lateinit var numbers: Array2D<Int>

    private var marked = Array2D(5,5, false)

    var hasWon: Boolean = false

    fun markNumber(ball: Int) {
        val coord = this.findPosOfNumber(ball)
        if (coord != null) this.marked[coord] = true
    }

    private fun findPosOfNumber(ball: Int): Coord? {
        val res = numbers.findIndexedByCoordinate { c, v -> v==ball}
        if (res == null) return null
        return res.first
    }

    fun checkBingo(): Boolean {
        return allRowIndices.any { this.checkRow(it) }
    }

    private fun checkRow(indices: Array<Coord>): Boolean {
        return indices.all { this.marked[it] }
    }

    fun getScore(): Int {
        val coordsOfUnmarked = marked.mapAndFilterToListByNotNull { c, marked -> if (marked) null else c }
        return coordsOfUnmarked.sumOf { this.numbers[it] }
    }

    companion object {
        /* Daft hard coding. */
        private val allRowIndices: List<Array<Coord>> = listOf(
            arrayOf(Coord(0, 0), Coord(0, 1), Coord(0, 2), Coord(0, 3), Coord(0, 4)),
            arrayOf(Coord(1, 0), Coord(1, 1), Coord(1, 2), Coord(1, 3), Coord(1, 4)),
            arrayOf(Coord(2, 0), Coord(2, 1), Coord(2, 2), Coord(2, 3), Coord(2, 4)),
            arrayOf(Coord(3, 0), Coord(3, 1), Coord(3, 2), Coord(3, 3), Coord(3, 4)),
            arrayOf(Coord(4, 0), Coord(4, 1), Coord(4, 2), Coord(4, 3), Coord(4, 4)),

            arrayOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(3, 0), Coord(4, 0)),
            arrayOf(Coord(0, 1), Coord(1, 1), Coord(2, 1), Coord(3, 1), Coord(4, 1)),
            arrayOf(Coord(0, 2), Coord(1, 2), Coord(2, 2), Coord(3, 2), Coord(4, 2)),
            arrayOf(Coord(0, 3), Coord(1, 3), Coord(2, 3), Coord(3, 3), Coord(4, 3)),
            arrayOf(Coord(0, 4), Coord(1, 4), Coord(2, 4), Coord(3, 4), Coord(4, 4)),
            // No diagonals
            //arrayOf( Coord(0,0), Coord(1,1), Coord(2,2), Coord(3,3) , Coord(4,4)),
            //arrayOf( Coord(0,4), Coord(1,3), Coord(2,2), Coord(3,1) , Coord(4,0)),
        )
    }
}

class Day04Problem : DailyProblem<Long>() {
    override val number = 4
    override val year = 2021

    override val name = "Giant Squid"

    override fun part1(): Long {
        val data: Pair<List<Int>, List<BingoBoard>> = parseBingoFile(this.getInputFile().absolutePath)

        val (balls, boards) = data

        for (ball in balls) {
            for (board in boards) {
                board.markNumber(ball)
                if (board.checkBingo()) {
                    val score = board.getScore()
                    return (ball * score).toLong()
                }
            }
        }
        throw Exception("No bingo")
    }
    override fun part2(): Long {
        val data: Pair<List<Int>, List<BingoBoard>> = parseBingoFile(this.getInputFile().absolutePath)

        val (balls, boards) = data

        val numberOfBoards = boards.size
        var numberOfWins = 0

        for (ball in balls) {
            for (board in boards) {
                if (board.hasWon) continue
                board.markNumber(ball)
                if (board.checkBingo()) {
                    board.hasWon = true
                    numberOfWins++
                    if (numberOfWins == numberOfBoards) {
                        val score = board.getScore()
                        return (ball * score).toLong()
                    }
                }
            }
        }
        throw Exception("No bingo")
    }
}

val day04Problem = Day04Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day04Problem.commonParts()
    day04Problem.runBoth(10)
}

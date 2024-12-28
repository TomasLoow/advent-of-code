package aoc.year2024

import DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Direction
import aoc.utils.parseCharArray
import kotlin.time.ExperimentalTime

class Day4Problem : DailyProblem<Int>() {

    override val number = 4
    override val year = 2024
    override val name = "Ceres Search"

    private lateinit var grid: Array2D<Char>
    private val word="XMAS"
    override fun commonParts() {
        grid = parseCharArray(getInputText())
    }


    override fun part1(): Int {
        val starts = grid.filterToList { _, c -> c == 'X' }
        return starts.sumOf { (c, _) ->
            Direction.entries.count { d -> grid[c, d, 4].joinToString("") == word }
        }
    }

    override fun part2(): Int {
        val positionsOfAs = grid.filterToList { _, char -> char == 'A' }
        return positionsOfAs
            .filter{ (c,_) -> !grid.onEdge(c)}
            .count { (c, _) ->
                val backSlash = listOf(grid[c.stepInDir(Direction.UPRIGHT)], grid[c.stepInDir(Direction.DOWNLEFT)]).joinToString("")
                val fwrdSlash = listOf(grid[c.stepInDir(Direction.UPLEFT)], grid[c.stepInDir(Direction.DOWNRIGHT)]).joinToString("")

                val masSam = setOf("MS", "SM")
                masSam.contains(backSlash) && masSam.contains(fwrdSlash)
            }
    }
}

val day4Problem = Day4Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day4Problem.runBoth(timesToRun = 100)
}
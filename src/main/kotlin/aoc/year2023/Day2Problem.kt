package aoc.year2023

import DailyProblem
import aoc.utils.nonEmptyLines
import kotlin.time.ExperimentalTime

typealias Game = Pair<Int,List<Map<String, Int>>>

class Day2Problem : DailyProblem<Int>() {
    override val number = 2
    override val year = 2023
    override val name = "Cube Conundrum"

    private lateinit var data: List<Game>

    override fun commonParts() {
        fun parseLine(idx: Int, line: String): Game {
            val reveals = line.dropWhile { it != ':' }.drop(2) // This drops the "Game 42: " beginnings
                .split("; ")
                .map { reveal ->
                    reveal.split(", ").associate { cube ->
                        val (count, color) = cube.split(" ")
                        Pair(color, count.toInt())
                    }
                }
            return Pair(idx, reveals)
        }
        data = getInputText().nonEmptyLines().mapIndexed(::parseLine)
    }

    private fun possibleGame(g: Game, availableCubes: Map<String, Int>): Boolean {
        val reveals = g.second
        return reveals.all { reveal ->
            reveal.all { (color, count) ->
                count <= availableCubes[color]!!
            }
        }
    }

    private val availableCubesPart1 = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    override fun part1(): Int {
        return data.filter { game ->
            possibleGame(game, availableCubesPart1)
        }.sumOf { it.first + 1 }
    }


    private fun minimumCubes(game: Game): Map<String,Int> {
        val reveals = game.second
        val res = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        reveals.forEach { reveal ->
            reveal.forEach { (color, count) ->
                res[color] = Math.max(res[color]!!, count)
            }
        }
        return res
    }

    private fun score(cubes: Map<String,Int>) : Int {
        return cubes.values.reduce { acc, i -> acc * i }
    }

    override fun part2(): Int {
        return data.sumOf { game -> score(minimumCubes(game))}
    }
}


val day2Problem = Day2Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    println(day2Problem.runBoth(timesToRun = 100))
}



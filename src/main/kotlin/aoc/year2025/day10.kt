package aoc.year2025

import aoc.DailyProblem
import aoc.utils.algorithms.BFS
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseOneLineOfSeparated
import kotlin.time.ExperimentalTime

data class Machine(
    val goal: String, val buttons: List<List<Int>>, val joltage: Array<Int>

) {
    fun size(): Int {
        return goal.length
    }

}

class Day10Problem : DailyProblem<Long>() {

    override val number = 10
    override val year = 2025
    override val name = "Factory"

    private lateinit var data: List<Machine>

    override fun commonParts() {
        fun parseMachine(line: String): Machine {
            val sep1 = line.indexOfFirst { it == ' ' }
            val sep2 = line.indexOfLast { it == ' ' }
            val goalString = line.take(sep1)
            val buttonsString = line.substring(sep1 + 1, sep2)
            val joltageString = line.drop(sep2 + 1)

            val goal = goalString.drop(1).dropLast(1)
            val size = goal.length
            val buttons = parseOneLineOfSeparated(buttonsString, { buttons ->
                buttons.drop(1).dropLast(1).split(",").map { it.toInt() }
            }, " ")

            val joltage = Array(size) { 0 }
            joltageString.drop(1).dropLast(1).split(",").forEachIndexed { i, s -> joltage[i] = s.toInt() }
            return Machine(goal, buttons, joltage)
        }
        data = getInputText().nonEmptyLines().map(::parseMachine)

    }

    class Part1BFS(val machine: Machine) : BFS<String>({ it == machine.goal }) {
        override fun reachable(state: String): Collection<String> {
            return machine.buttons.map { buttons ->
                val new = state.mapIndexed { i, c ->
                    if (i !in buttons) c else {
                        if (c == '#') '.' else '#'
                    }
                }.joinToString("")
                new
            }
        }
    }

    override fun part1(): Long {
        var res = 0L
        for (machine in data) {
            val s = machine.size()
            val bfs = Part1BFS(machine)
            val start = ".".repeat(s)
            val sol = bfs.solve(start)
            res += sol.size - 1
            println(sol.size - 1)
        }
        return res
    }


    class Part2BFS(val machine: Machine) : BFS<List<Int>>({ it == machine.joltage }) {
        override fun reachable(state: List<Int>): Collection<List<Int>> {
            val reachable = machine.buttons.map { buttons ->
                val new = state.mapIndexed { i, c -> if (i in buttons) c + 1 else c }
                new
            }.filter { candidate ->
                candidate.zip(machine.joltage).all { (a, b) -> a <= b }
            }
            return reachable
        }
    }


    override fun part2(): Long {
        var res = 0L
        for (machine in data) {
            val s = machine.size()
            val bfs = Part2BFS(machine)
            val start = buildList { repeat(s) { add(0) } }
            val sol = bfs.solve(start)
            res += sol.size - 1
            println(sol.size - 1)
        }
        return res
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.testData = true
    day10Problem.runBoth(1)
}
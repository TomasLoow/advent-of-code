package aoc2022

import DailyProblem
import utils.nonEmptyLines
import java.io.File
import kotlin.time.ExperimentalTime

class Day5Problem(override val inputFilePath: String) : DailyProblem<String> {

    override val number = 5
    override val name = "Supply Stacks"

    private fun parseFile(): Pair<Array<ArrayDeque<Char>>, List<Triple<Int, Int, Int>>> {
        val (stackpart, movepart) = File(inputFilePath).readText().split("\n\n")

        val moveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        val moves = movepart.nonEmptyLines().map { line ->
            val (count, from, to) = moveRegex.matchEntire(line)!!.destructured
            Triple(count.toInt(), from.toInt(), to.toInt())
        }

        val numStacks = (stackpart.nonEmptyLines().maxOf { it.length } + 1) / 4
        val stacks = Array<ArrayDeque<Char>>(numStacks) { ArrayDeque() }
        stackpart.nonEmptyLines().reversed().drop(1).forEach { line ->
            repeat(numStacks) { idx ->
                val pos = 4 * idx + 1
                if (pos < line.length) {
                    val c = line[pos]
                    if (c != ' ') {
                        stacks[idx].addFirst(c)
                    }
                }
            }
        }
        return Pair(stacks, moves)
    }



    override fun part1(): String {
        val (stacks, moves) = parseFile()
        moves.forEach { (count, from, to) ->
            repeat(count) {
                stacks[to-1].addFirst(stacks[from-1].removeFirst())
            }
        }
        return stacks.map { it.first() }.joinToString("")
    }


    override fun part2(): String {
        val (stacks, moves) = parseFile()
        moves.forEach { (count, from, to) ->
            val toStack = stacks[to - 1]
            val fromStack = stacks[from - 1]
            val moved = buildList<Char> {
                repeat(count) {
                    add(fromStack.removeFirst())
                }
            }.reversed()
            moved.forEach {toStack.addFirst(it)}
        }
        return stacks.map { it.first() }.joinToString("")
    }
}

val day5Problem = Day5Problem("input/aoc2022/day5.txt")

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.runBoth(100)
}
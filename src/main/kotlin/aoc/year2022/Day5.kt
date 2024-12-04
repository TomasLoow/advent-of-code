package aoc.year2022

import DailyProblem
import aoc.utils.nonEmptyLines
import aoc.utils.parseTwoBlocks
import kotlin.time.ExperimentalTime

class Day5Problem : DailyProblem<String>() {

    override val number = 5
    override val year = 2022
    override val name = "Supply Stacks"

    private fun parseStacks(stackpart: String): Array<ArrayDeque<Char>> {
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
        return stacks
    }

    private fun parseMoves(movepart: String): List<Triple<Int, Int, Int>> {
        val moveRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        val moves = movepart.nonEmptyLines().map { line ->
            val (count, from, to) = moveRegex.matchEntire(line)!!.destructured
            Triple(count.toInt(), from.toInt(), to.toInt())
        }
        return moves
    }

    private fun parseFile(): Pair<Array<ArrayDeque<Char>>, List<Triple<Int, Int, Int>>> {
        return parseTwoBlocks(getInputText(), ::parseStacks, ::parseMoves)
    }


    private fun moveOneAtTheTime(
        count: Int,
        fromStack: ArrayDeque<Char>,
        toStack: ArrayDeque<Char>,
    ) {
        repeat(count) {
            toStack.addFirst(fromStack.removeFirst())
        }
    }

    private fun moveMany(
        count: Int,
        fromStack: ArrayDeque<Char>,
        toStack: ArrayDeque<Char>
    ) {
        fromStack.take(count).reversed().forEach{toStack.addFirst(it)}
        repeat(count) { fromStack.removeFirst() }
    }

    override fun part1(): String {
        val (stacks, moves) = parseFile()
        moves.forEach { (count, from, to) ->
            val toStack = stacks[to - 1]
            val fromStack = stacks[from - 1]
            moveOneAtTheTime(count, fromStack, toStack)
        }
        return stacks.map { it.first() }.joinToString("")
    }

    override fun part2(): String {
        val (stacks, moves) = parseFile()
        moves.forEach { (count, from, to) ->
            val toStack = stacks[to - 1]
            val fromStack = stacks[from - 1]
            moveMany(count, fromStack, toStack)
        }
        return stacks.map { it.first() }.joinToString("")
    }
}


val day5Problem = Day5Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day5Problem.runBoth(1000)
}
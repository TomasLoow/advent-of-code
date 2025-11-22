package aoc.year2021

import aoc.DailyProblem
import aoc.utils.parseListOfPairs
import java.lang.Integer.parseInt


enum class Command {
    Forward,
    Down,
    Up,
}

typealias CommandLine = Pair<Command, Int>


private typealias SubmarineState = Pair<Int, Int>
private typealias SubmarineStateWithDelta = Triple<Int, Int, Int>


class Day02Problem : DailyProblem<Long>() {
    override val number = 2
    override val year = 2021
    override val name = "Dive!"

    private lateinit var commands: List<Pair<Command, Int>>

    override fun commonParts() {
        commands = parseCommandFile()
    }

    private fun parseCommandFile(): List<CommandLine> {
        fun parseCommand(s: String): Command {
            return when (s) {
                "forward" -> Command.Forward
                "up" -> Command.Up
                "down" -> Command.Down
                else -> throw Exception("Bad Command")
            }
        }
        return parseListOfPairs(getInputText(), ::parseCommand, ::parseInt)
    }

    /* Move one step with the rules from part 1 of the problem */
    private fun updatePos(state: SubmarineState, cmd: CommandLine): SubmarineState {
        val (command, arg) = cmd
        val (x, depth) = state
        return when (command) {
            Command.Forward -> SubmarineState(x + arg, depth)
            Command.Up -> SubmarineState(x, depth - arg)
            Command.Down -> SubmarineState(x, depth + arg)
        }
    }

    /* Move one step with the rules from part 2 of the problem */
    private fun updatePosWithDelta(state: SubmarineStateWithDelta, cmd: CommandLine): SubmarineStateWithDelta {
        val (command, arg) = cmd
        val (x, depth, delta) = state
        return when (command) {
            Command.Forward -> SubmarineStateWithDelta(x + arg, depth + delta * arg, delta)
            Command.Up -> SubmarineStateWithDelta(x, depth, delta - arg)
            Command.Down -> SubmarineStateWithDelta(x, depth, delta + arg)
        }
    }

    override fun part1(): Long {
        val initialState = SubmarineState(0, 0)
        val (x, depth) = commands.fold(initialState) { pos, cmd -> updatePos(pos, cmd) }

        return (x * depth).toLong()
    }

    override fun part2(): Long {
        val initialState = SubmarineStateWithDelta(0, 0, 0)
        val (x, depth, _) = commands.fold(initialState) { pos, cmd -> updatePosWithDelta(pos, cmd) }

        return (x * depth).toLong()
    }
}

val day02Problem = Day02Problem()

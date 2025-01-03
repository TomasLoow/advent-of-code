package aoc.year2022

import DailyProblem
import aoc.utils.emptyMutableSet
import aoc.utils.extensionFunctions.mutate
import aoc.utils.extensionFunctions.mutateImp
import aoc.utils.extensionFunctions.rotateLeft
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.geometry.Rect
import kotlin.collections.plus
import kotlin.time.ExperimentalTime

//TODO very slow solution. A rewrite would make sense.

val rules = listOf<(Collection<Coord>, Coord) -> Coord?>(
    { elves, elf ->
        if (elf + Direction.UP !in elves && elf + Direction.UPRIGHT !in elves && elf + Direction.UPLEFT !in elves) {
            elf + Direction.UP
        } else null
    },
    { elves, elf ->
        if (elf + Direction.DOWN !in elves && elf + Direction.DOWNRIGHT !in elves && elf + Direction.DOWNLEFT !in elves) {
            elf + Direction.DOWN
        } else null
    },
    { elves, elf ->
        if (elf + Direction.LEFT !in elves && elf + Direction.UPLEFT !in elves && elf + Direction.DOWNLEFT !in elves) {
            elf + Direction.LEFT
        } else null
    },
    { elves, elf ->
        if (elf + Direction.RIGHT !in elves && elf + Direction.UPRIGHT !in elves && elf + Direction.DOWNRIGHT !in elves) {
            elf + Direction.RIGHT
        } else null
    },
)

class Day23Problem : DailyProblem<Int>() {

    override val number = 23
    override val year = 2022
    override val name = "Unstable Diffusion"

    private lateinit var data: Set<Coord>

    override fun commonParts() {
        val a = Array2D.parseFromLines(getInputText()) { it == '#' }
        data = a.filterToList { c, v -> v }.map { it -> it.first }.toSet()
    }

    private fun applyRules(rules: List<(Collection<Coord>, Coord) -> Coord?>, elves: Set<Coord>, elf: Coord, rotate: Int): Coord {
        try {
            return rules.firstNotNullOf { it(elves, elf) }
        } catch (e: NoSuchElementException) {
            return elf
        }
    }

    private fun simulateRound(elves: Set<Coord>, step: Int): Pair<Set<Coord>, Boolean> {
        var anyoneMoved = false
        val newPositions = mutableMapOf<Coord, MutableSet<Coord>>()
        val activeRules = rules.rotateLeft(step)
        for (elf in elves) {
            if (! elf.neighbours(diagonal = true).any { it in elves }) {
                newPositions[elf]=mutableSetOf(elf)
            } else {
                val c = applyRules(activeRules, elves, elf, step)
                newPositions.mutateImp(c, emptyMutableSet()) { it.add(elf) }
                anyoneMoved = true
            }
        }
        val newElves = buildSet<Coord> {
            newPositions.forEach { (coord, list) ->
                if (list.size == 1) {
                    add(coord)
                } else {
                    addAll(list)
                }
            }
        }
        return newElves to anyoneMoved
    }

    override fun part1(): Int {
        var elves = data
        repeat(10) { step ->
            elves = simulateRound(elves, step).first
        }
        val r = Rect.bounding(elves)
        return r.area - elves.size
    }


    override fun part2(): Int {
        var elves = data
        var step = 0
        while (true) {
            val go = simulateRound(elves, step)
            elves = go.first
            step++
            if (! go.second) { return step }
        }
    }
}


val day23Problem = Day23Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day23Problem.testData = false
    day23Problem.runBoth(20)
}
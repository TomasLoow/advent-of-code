package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.parseBlockList
import kotlin.time.ExperimentalTime

private data class Button(val x: Long, val y: Long)
private data class Prize(val x: Long, val y: Long)
private data class Machine(val a: Button, val b: Button, val prize: Prize)


class Day13Problem : DailyProblem<Long>() {

    override val number = 13
    override val year = 2024
    override val name = "Claw Contraption"

    private lateinit var data: List<Machine>

    override fun commonParts() {
        fun parseMachine(s: String): Machine {
            val lines = s.nonEmptyLines()
            check(lines.size == 3)
            val a = lines[0].removePrefix("Button A: X+").split(", Y+").map { it.trim().toInt() }.let { Button(it[0].toLong(),it[1].toLong()) }
            val b = lines[1].removePrefix("Button B: X+").split(", Y+").map { it.trim().toInt() }.let { Button(it[0].toLong(),it[1].toLong()) }
            val prize = lines[2].removePrefix("Prize: X=").split(", Y=").map { it.trim().toInt() }.let { Prize(it[0].toLong(),it[1].toLong()) }
            return Machine(a,b,prize)
        }
        data = parseBlockList(getInputText(), ::parseMachine)
    }


    override fun part1(): Long {
        return data.sumOf {  solve(it) }
    }

    override fun part2(): Long {
        val big = "10000000000000".toLong()
        return data.map { m -> m.copy(prize = Prize(m.prize.x + big, y = m.prize.y + big))}.sumOf {  solve(it) }
    }

    private fun solve(m: Machine): Long {
        // The machine is actually a discrete linear equation system in two variables
        val ax = m.a.x
        val ay = m.a.y
        val bx = m.b.x
        val by = m.b.y

        val tx = m.prize.x
        val ty = m.prize.y

        val determinant = (ax*by - ay*bx)
        val ad = tx * by - ty * bx
        val bd = ty * ax - tx * ay
        if (ad % determinant != 0L) return 0L // impossible to make first row 0
        if (bd % determinant != 0L) return 0L // impossible to make second row 0
        val a = ad / determinant
        val b = bd / determinant
        return 3.toLong()*a+b
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.runBoth(100)
}
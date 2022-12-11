package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day2Problem() : DailyProblem<Int>() {

    override val number = 2
    override val year = 2015
    override val name = "I Was Told There Would Be No Math"

    lateinit var data : List<Triple<Int,Int,Int>>

    override fun commonParts() {
        data = getInputText().nonEmptyLines().map { line ->
            val (x,y,z) =line.split("x").map(::parseInt).sorted()
            Triple(x,y,z)
        }
    }
    override fun part1(): Int {
        return data.sumOf { (x,y,z) ->
            3*x*y + 2*(x+y)*z
        }
    }


    override fun part2(): Int {
        return data.sumOf { (x, y, z) ->
            val around = 2*(x+y)
            val bow = x*y*z
            around+bow
        }
    }
}

val day2Problem = Day2Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day2Problem.runBoth(100)
}
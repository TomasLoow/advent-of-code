package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day19Problem() : DailyProblem<Int>() {

    override val number = 19
    override val year = 2015
    override val name = "Medicine for Rudolph"

    private lateinit var rules: List<Pair<String, String>>
    private lateinit var start: String

    override fun commonParts() {

        val (r, s) = parseTwoBlocks(getInputText(), { rules ->
            parseListOfPairs(rules, ::id, ::id, " => ")
        }, ::id)
        start = s
        rules = r
    }

    fun applyRule(s: String, atom: String, production: String): List<String> {
        val matches = atom.toRegex().findAll(s)
        return matches.map { match ->
            s.subSequence(0, match.range.first).toString() + production + s.subSequence(
                match.range.last + 1,
                s.lastIndex
            ).toString()
        }.toList()
    }

    override fun part1(): Int {
        return rules.flatMap { (k, v) ->
            applyRule(start, k, v)
        }.toSet().size
    }


    override fun part2(): Int {
        // val start = "CRnFYFYFArSiRnFArMg"
        val numAtoms = start.count { it.isUpperCase() }
        val numAr = start.windowed(2).count { it == "Ar" }
        val numY = start.count { it == 'Y' }

        /*
        every move will either replace one atom with two atoms, with .Rn.Ar or .Rn.Y.Ar
        call the number of such moves x,y,x we get the system of equations
        numY  =              z
        numAr =         y +  z
        numAtoms = x + 3y + 5z

        solving this gives
        z =                      numY
        y =             numAr -  numY
        x = numAtoms - 3numAr - 2numY

        therefor the total number of moves is x+y+z = numAtoms -2numAr - 2numY
        */

        val steps = numAtoms - 2 * numAr - 2 * numY
        return steps - 1 //Minus one for the free "e" at the beginning
    }
}

val day19Problem = Day19Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day19Problem.runBoth(100)
}
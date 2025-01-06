package aoc.year2019

import DailyProblem
import aoc.utils.extensionFunctions.isAscending
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

class Day04Problem : DailyProblem<Int>() {

    override val number = 4
    override val year = 2019
    override val name = "Secure Container"

    private var l by Delegates.notNull<Int>()
    private var u by Delegates.notNull<Int>()
    private lateinit var ascending: List<String>

    override fun commonParts() {
        getInputText().lines().first().split("-").let { (a, b) ->
            l = a.toInt()
            u = b.toInt()
        }

        ascending = (l..u).mapNotNull { n ->
            val s = n.toString()
            if (s.isAscending()) s else null
        }

    }

    override fun part1(): Int {
        return ascending.count { s ->
            listOf("00", "11", "22", "33", "44", "55", "66", "77", "88", "99").any { it in s }
        }
    }

    override fun part2(): Int {
        return ascending.count { s ->
            "0123456789".any { c -> s.count { it == c } == 2 }
        }
    }
}


val day04Problem = Day04Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day04Problem.testData = false
    day04Problem.runBoth(100)
}
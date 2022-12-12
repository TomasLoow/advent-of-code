package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

private typealias Aunt = Map<String, Int>
class Day16Problem() : DailyProblem<Int>() {

    override val number = 16
    override val year = 2015
    override val name = "Aunt Sue"

    private lateinit var aunts: List<Aunt>

    private val searchPattern = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1,
    )

    override fun commonParts() {
        val re =  """([a-z]+): (\d+)""".toRegex()

        aunts = getInputText().nonEmptyLines().map {
            buildMap {
                re.findAll(it).forEach { match ->
                    val (prop, value) = match.destructured
                    this[prop] = value.toInt()
                }
            }
        }
    }


    private fun matchesPart1(searchPattern: Aunt, aunt: Aunt) : Boolean{
        return searchPattern.all { (prop, value) ->
            (prop !in aunt) || aunt[prop] == value
        }
    }

    private fun matchesPart2(searchPattern: Aunt, aunt: Aunt) : Boolean{
        return searchPattern.all { (prop, patternValue) ->
            if (prop !in aunt) {
                true
            } else {
                val propVal = aunt[prop]!!
                when (prop) {
                    in listOf("cats", "trees") -> propVal > patternValue
                    in listOf("pomeranians","goldfish") -> propVal < patternValue
                    else -> propVal == patternValue
                }
            }
        }
    }

    override fun part1(): Int {
        val idx = aunts.indexOfFirst { aunt ->
            matchesPart1(searchPattern, aunt)
        }
        return idx+1

    }


    override fun part2(): Int {
        val idx = aunts.indexOfFirst { aunt ->
            matchesPart2(searchPattern, aunt)
        }
        return idx+1
    }
}

val day16Problem = Day16Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day16Problem.runBoth(100)
}
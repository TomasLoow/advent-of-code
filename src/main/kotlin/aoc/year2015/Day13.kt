package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day13Problem() : DailyProblem<Int>() {

    override val number = 13
    override val year = 2015
    override val name = "Knights of the Dinner Table"

    private lateinit var rules: Map<Pair<String,String>, Int>
    private lateinit var persons: List<String>

    override fun commonParts() {
        fun parseChange(s: String): Int {
            if ("lose" in s) {
                return -s.substringAfter("lose ").toInt()
            }
            return s.substringAfter("gain ").toInt()
        }
        val personsSet = mutableSetOf<String>()

        rules = parseListOfTriples(
            getInputText(),
            ::id,
            ::parseChange,
            { it.dropLast(1) },
            " would ",
            " happiness units by sitting next to "
        ).map { (a, change, b) ->
            personsSet.add(a)
            personsSet.add(b)
            Pair(Pair(a,b), change)
        }.toMap()
        this.persons = personsSet.toList()
    }


    private fun score(perm: List<String>, rules: Map<Pair<String,String>, Int>):Int {
        val allNeighbours = perm.windowed(2) + listOf(listOf(perm.first(), perm.last()))
        return allNeighbours.sumOf { window ->
            val (a,b) = window
            rules[Pair(a,b)]!! + rules[Pair(b,a)]!!
        }
    }

    fun rotationInvariantPermutations(persons: List<String>): Sequence<List<String>> {
        val someGuy = persons.first()
        return persons.drop(1).permutationsSequence().map { perm ->  listOf(someGuy) + perm }
    }
    override fun part1(): Int {
        return rotationInvariantPermutations(persons).maxOf { perm ->
            score(perm, rules)
        }
    }


    override fun part2(): Int {
        val persons2 = persons + MY_NAME
        val rules2 = rules.toMutableMap()
        persons.forEach { p ->
            rules2[Pair(MY_NAME, p)] = 0
            rules2[Pair(p, MY_NAME)] = 0
        }
        return rotationInvariantPermutations(persons2).maxOf { perm ->
            score(perm, rules2)
        }
    }

    companion object {
        private var MY_NAME = "Me"
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.runBoth(100)
}
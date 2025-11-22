package aoc.year2019

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

data class Planet(val name: String, var orbitsAround: Planet?) {
    val countParents: Int by lazy {
            if (orbitsAround == null) 0
            else 1 + orbitsAround!!.countParents
        }

    val parents: List<Planet> by lazy {
            if (orbitsAround == null) emptyList()
            else orbitsAround!!.parents + orbitsAround!!
        }

}

class Day06Problem : DailyProblem<Int>() {

    override val number = 6
    override val year = 2019
    override val name = "Universal Orbit Map"

    private lateinit var planetsMap: MutableMap<String, Planet>

    override fun commonParts() {
        planetsMap = mutableMapOf()
        getInputText().nonEmptyLines().forEach { line ->
            val (a, b) = line.split(")")
            if (a !in planetsMap) {
                planetsMap[a] = Planet(a, null)
            }
            if (b !in planetsMap) {
                planetsMap[b] = Planet(b, planetsMap[a])
            } else {
                planetsMap[b]!!.orbitsAround = planetsMap[a]
            }
        }
    }


    override fun part1(): Int {
        return planetsMap.values.sumOf { it.countParents }
    }


    override fun part2(): Int {
        val you = planetsMap["YOU"]!!
        val santa = planetsMap["SAN"]!!
        val commonParent = you.parents.last { it in santa.parents }
        return (you.parents.size - you.parents.indexOf(commonParent)) + (santa.parents.size - santa.parents.indexOf(commonParent)) -2
    }
}

val day06Problem = Day06Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day06Problem.testData = false
    day06Problem.runBoth(1)
}
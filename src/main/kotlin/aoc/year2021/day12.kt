package aoc.year2021

import aoc.DailyProblem
import aoc.utils.parseListOfPairs
import java.util.*


/** Encoding: Caves are stored as ints instead of strings.
 * We always have 0 = start, 1 = end. Other caves are arbitrary wih large, caves that can be revisited mapped to
 * negative numbers and small normal caves mapped to positive numbers.
 */
typealias Cave = Int
const val startCave:Cave = 0
const val endCave:Cave = 1


typealias CaveSystem = Map<Cave, List<Cave>>

class Day12Problem : DailyProblem<Long>() {
    override val number = 12
    override val year = 2021
    override val name = "Passage Pathing"

    private lateinit var map: CaveSystem

    override fun commonParts() {
        map = parseConnections()
    }

    private fun allowsRevisits(cave: Cave): Boolean {
        return cave < 0
    }

    private fun cavesReachableFrom(map: CaveSystem, cave: Cave): Collection<Cave> {
        return map[cave]!!
    }

    private fun parseConnections(): CaveSystem {
        val caveNameRegistry: MutableMap<String, Int> = mutableMapOf("start" to startCave, "end" to endCave)
        var caveNumberCounter = 2
        val allCaves = mutableSetOf(startCave, endCave)

        fun handleCave(caveName: String): Cave {
            val cave: Cave
            if (caveNameRegistry.contains(caveName)) {
                cave = caveNameRegistry[caveName]!!
            } else {
                cave = if (caveName != caveName.uppercase()) {
                    caveNumberCounter
                } else {
                    -caveNumberCounter
                }
                caveNameRegistry[caveName] = cave
                allCaves.add(cave)
                caveNumberCounter++
            }
            return cave
        }
        val connections = parseListOfPairs(getInputText(), ::handleCave, ::handleCave, separator = "-")
        return buildList {
            allCaves.forEach { cave ->
                val connected = buildList {
                    connections.forEach { connection ->
                        if (connection.first == cave) {
                            add(connection.second)
                        } else if (connection.second == cave) {
                            add(connection.first)
                        }
                    }
                }.toList()
                add(Pair(cave, connected))
            }
        }.toMap()
    }

    private data class SearchState(val allowRevisit: Boolean, val visited: List<Cave>, val currentNode: Cave)

    private fun search(allowRevisits: Boolean) : Int {
        val initialState = SearchState(allowRevisits, listOf(), startCave)
        val searchStack = Stack<SearchState>()
        var foundPaths = 0

        searchStack.push(initialState)
        while (searchStack.isNotEmpty()) {
            val (allowRevisit, visited, currentNode) = searchStack.pop()
            if (currentNode == endCave) {
                foundPaths++
                continue
            }
            val allConnectedCaves = cavesReachableFrom(map, currentNode)
            val possibleNormalNextSteps =  allConnectedCaves.filter { !visited.contains(it) || allowsRevisits(it)  }
            val possibleBonusRevisitSteps =
                if (allowRevisit) {
                    allConnectedCaves
                        .filter { visited.contains(it) && it != startCave }
                        .filter { !possibleNormalNextSteps.contains(it)}
                } else {
                    emptyList()
                }

            val newVisitedSet = if (allowsRevisits(currentNode)) {
                visited
            } else {
                visited.plus(currentNode)
            }

            possibleNormalNextSteps.forEach {
                searchStack.push(SearchState(allowRevisit, newVisitedSet, it))
            }
            possibleBonusRevisitSteps.forEach {
                searchStack.push(SearchState(false, newVisitedSet, it))
            }
        }
        return foundPaths
    }

    override fun part1(): Long {
        return search(allowRevisits = false).toLong()
    }

    override fun part2(): Long {
        return search(allowRevisits = true).toLong()
    }
}

val day12Problem = Day12Problem()

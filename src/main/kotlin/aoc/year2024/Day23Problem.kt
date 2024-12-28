package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.allUnorderedPairs
import aoc.utils.emptyMutableSet
import aoc.utils.extensionFunctions.mutate
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day23Problem : DailyProblem<String>() {

    override val number = 23
    override val year = 2024
    override val name = "LAN Party"

    private lateinit var data: Map<String, Set<String>>

    override fun commonParts() {
        data = buildMap {
            getInputText().nonEmptyLines().forEach { line ->
                val (a, b) = line.split("-")
                mutate(a, emptySet()) { it + b }
                mutate(b, emptySet()) { it + a }
            }
        }
    }

    private fun findTriplets(): MutableSet<Triple<String, String, String>> {
        val res = mutableSetOf<Triple<String, String, String>>()
        data.keys.toList().allUnorderedPairs().filter { (a, b) -> a in data[b]!! }.forEach { (a, b) ->
            data[a]!!.intersect(data[b]!!).forEach { c ->
                val (x, y, z) = setOf(a, b, c).sorted()
                res.add(Triple(x, y, z))
            }
        }
        return res
    }

    override fun part1(): String {
        val tps = findTriplets().filter { triplet -> triplet.toList().any { computer -> computer.startsWith("t") } }
        return tps.count().toString()
    }

    /**
    Finds all cliques in the graph using the Bron-Kerbosh algorithm
    https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm

     */
    private fun findCliques(graph: Map<String, Set<String>>): List<Set<String>> {
        val cliques = mutableListOf<Set<String>>()
        val nodes = graph.keys.toMutableSet()

        /* Inner recursive helper */
        fun run(rSet: Set<String>, pSet: MutableSet<String>, xSet: MutableSet<String>) {
            if (pSet.isEmpty() && xSet.isEmpty()) {
                cliques.add(rSet)
            } else {
                for (v in pSet.toList()) {
                    val newR = rSet + v
                    if (v !in graph) {
                        run(newR, emptyMutableSet(), emptyMutableSet())
                        continue
                    }
                    val newP = pSet.intersect(graph[v]!!).toMutableSet()
                    val newX = xSet.intersect(graph[v]!!).toMutableSet()
                    run(newR, newP, newX)
                    pSet.remove(v)
                    xSet.add(v)
                }
            }
        }

        run(emptySet(), nodes, mutableSetOf())
        return cliques
    }

    override fun part2(): String {
        val c = findCliques(data)
        return c.maxBy { it.size }.sorted().joinToString(",")
    }
}

val day23Problem = Day23Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day23Problem.testData = false
    day23Problem.runBoth(20)
}
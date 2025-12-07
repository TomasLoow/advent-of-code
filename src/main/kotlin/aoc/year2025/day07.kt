package aoc.year2025

import aoc.DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

class Day07Problem : DailyProblem<Long>() {

    override val number = 7
    override val year = 2025
    override val name = "Laboratories"

    private lateinit var map: List<Array<Boolean>>
    private var startX by Delegates.notNull<Int>()

    override fun commonParts() {
        val inputlines = getInputText().nonEmptyLines()
        startX = inputlines[0].indexOf('S')
        val lines = inputlines.drop(2).chunked(2).map { (a, _) -> a.map { it == '^' }.toTypedArray() }
        map = lines
    }

    override fun part1(): Long {
        var hitSplitters = 0
        var beams = setOf<Int>(startX)

        map.forEach { line ->
            val hitBeams = beams.filter { beam -> line[beam] }
            hitSplitters += hitBeams.size
            beams = beams - hitBeams + hitBeams.flatMap { listOf(it+1, it-1) }.toSet()

        }
        return hitSplitters.toLong()
    }

    val worldCache = emptyMutableMap<Pair<Int,Int>, Long>()
    fun countWorlds(x: Int, row: Int): Long {
        if ((x to row) in worldCache) return worldCache[x to row]!!
        if (row >= map.size) return 1L

        val line = map[row]
        val res = if (line[x]) {
            countWorlds(x - 1, row+1) + countWorlds(x + 1, row+1)
        } else {
            countWorlds(x , row+1)
        }
        worldCache[x to row] = res
        return res
    }

    override fun part2(): Long {
        worldCache.clear()
        return countWorlds(startX, 0)
    }
}

val day07Problem = Day07Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day07Problem.testData = false
    day07Problem.runBoth(1000)
}
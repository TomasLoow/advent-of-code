package aoc.year2022

import DailyProblem
import aoc.utils.*
import aoc.utils.algorithms.BFS
import aoc.utils.algorithms.BFSNoPathFound
import aoc.utils.extensionFunctions.minAndMax
import java.lang.Integer.parseInt
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime

data class Block(val x: Int, val y: Int, val z: Int) {
    override fun hashCode(): Int {
        return x.shl(8) + y.shl(4) + z
    }

    override fun equals(other: Any?): Boolean { 
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Block
        return ((x == other.x) && (y == other.y) && (z == other.z))
    }
}

class Day18Problem : DailyProblem<Int>() {

    override val number = 18
    override val year = 2022
    override val name = "Boiling Boulders"

    private lateinit var data: Set<Block>

    override fun commonParts() {
        data = parseListOfTriples(getInputText(), ::parseInt, ::parseInt, ::parseInt, ",", ",").map {
            Block(it.first, it.second, it.third)
        }.toSet()
    }


    private fun touches(block: Block, it: Block): Boolean {
        return ((block.x - it.x).absoluteValue + (block.y - it.y).absoluteValue + (block.z - it.z).absoluteValue) == 1
    }

    override fun part1(): Int {
        return surfaceArea(data)
    }

    private fun surfaceArea(blocks: Collection<Block>): Int {
        val sharedEdges = emptyMutableSet<Pair<Block, Block>>()
        val seenBlocks = mutableListOf<Block>()
        blocks.forEach { block ->
            seenBlocks.forEach { if (touches(block, it)) sharedEdges.add(Pair(block, it)) }
            seenBlocks.add(block)
        }
        return seenBlocks.size * 6 - 2 * sharedEdges.size
    }


    override fun part2(): Int {
        val (minX, maxX) = data.map { it.x }.minAndMax()
        val (minY, maxY) = data.map { it.y }.minAndMax()
        val (minZ, maxZ) = data.map { it.z }.minAndMax()
        val xRange = minX - 1..maxX + 1
        val yRange = minY - 1..maxY + 1
        val zRange = minZ - 1..maxZ + 1
        val floodFillOutSide = floodFill(
            Block(xRange.first, yRange.first, zRange.first), xRange, yRange, zRange, data
        )

        val full = xRange.flatMap { x ->
            yRange.flatMap { y ->
                zRange.map { z -> Block(x, y, z) }
            }
        }.toSet()
        return surfaceArea(full - floodFillOutSide)
    }

    private fun floodFill(
        start: Block,
        xr: IntRange,
        yr: IntRange,
        zr: IntRange,
        blocked: Set<Block>
    ): Set<Block> {
        class FloodFillBFS : BFS<Block>({ false }) {
            override fun reachable(state: Block): Collection<Block> {
                return neigbours(state).filter { it.x in xr && it.y in yr && it.z in zr && it !in blocked }
            }
        }
        try {
            FloodFillBFS().solve(start)
            throw Exception("That should have failed...")
        } catch (e: BFSNoPathFound) {
            @Suppress("UNCHECKED_CAST")
            return e.explored as Set<Block>
        }
    }

    private fun neigbours(b: Block): List<Block> {
        return listOf(
            b.copy(x = b.x + 1),
            b.copy(x = b.x - 1),
            b.copy(y = b.y + 1),
            b.copy(y = b.y - 1),
            b.copy(z = b.z + 1),
            b.copy(z = b.z - 1),
        )
    }
}


val day18Problem = Day18Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day18Problem.runBoth(50)
}
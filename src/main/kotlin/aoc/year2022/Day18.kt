package aoc.year2022

import DailyProblem
import aoc.utils.*
import java.lang.Integer.*
import kotlin.math.absoluteValue
import kotlin.time.ExperimentalTime
data class Block(val x: Int, val y: Int, val z: Int) {
    override fun hashCode(): Int {
        return x.shl(8) + y.shl(4) + z
    }
}

class Day18Problem() : DailyProblem<Int>() {

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
        val sharedEdges = mutableSetOf<Pair<Block, Block>>()
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
        val floodFillOutSide = floodFill(
            Block(minX - 1, minY - 1, minZ - 1),
            (minX - 1..maxX + 1),
            (minY - 1..maxY + 1),
            (minZ - 1..maxZ + 1), data
        )

        val full = (minX - 1..maxX + 1).flatMap { x ->
            (minY - 1..maxY + 1).flatMap { y ->
                (minZ - 1..maxZ + 1).map { z -> Block(x, y, z) }
            }
        }.toSet()

        val blob = full-floodFillOutSide
        return surfaceArea(blob)
    }

    private fun floodFill(
        start: Block,
        xr: IntRange,
        yr: IntRange,
        zr: IntRange,
        blocked: Set<Block>
    ): MutableSet<Block> {
        val seen = mutableSetOf<Block>()
        val q = mutableSetOf<Block>()
        q.add(start)
        while (q.isNotEmpty()) {
            val current = q.first()
            q.remove(current)
            seen.add(current)
            val ns = neigbours(current)
            ns.forEach { n ->
                if (n.x in xr && n.y in yr && n.z in zr && (n !in seen) && (n !in blocked)) {
                    q.add(n)
                }
            }
        }
        return seen

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
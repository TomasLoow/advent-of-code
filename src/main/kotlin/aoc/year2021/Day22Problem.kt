package aoc.year2021

import DailyProblem
import aoc.utils.intersectionOrNull
import aoc.utils.length
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.time.ExperimentalTime

fun parseReactorFile(path: String) : List<SignedBlock> {
    val re = Regex("(on|off) x=(-?[0-9]+)\\.\\.(-?[0-9]+),y=(-?[0-9]+)\\.\\.(-?[0-9]+),z=(-?[0-9]+)\\.\\.(-?[0-9]+)")
    return File(path).readLines().map { line ->
        val reResult = re.find(line)!!.groupValues

        val sign: Sign = if (reResult[1] == "on") 1 else -1
        SignedBlock(sign, Block(
            (reResult[2].toInt()..reResult[3].toInt()),
            (reResult[4].toInt()..reResult[5].toInt()),
            (reResult[6].toInt()..reResult[7].toInt()),
        ))
    }
}


data class Block(
        val xrange: IntRange,
        val yrange: IntRange,
        val zrange: IntRange,
    ) {
    fun intersection(other: Block): Block? {
        val newXrange =  xrange.intersectionOrNull(other.xrange)?: return null
        val newYrange =  yrange.intersectionOrNull(other.yrange)?: return null
        val newZrange =  zrange.intersectionOrNull(other.zrange)?: return null
        return Block(newXrange, newYrange, newZrange)
    }
    fun size():Long {
        return xrange.length.toLong() * yrange.length.toLong() * zrange.length.toLong()
    }
}

typealias Sign = Int
typealias SignedBlock = Pair<Sign, Block>

class Day22Problem : DailyProblem<Long>() {
    override val number = 22
    override val year = 2021
    override val name = "Reactor Reboot"

    private lateinit var inputSquares: List<SignedBlock>

    override fun commonParts() {
        inputSquares = parseReactorFile(getInputFile().absolutePath)
    }

    override fun part1(): Long {
        return combineBlocksAndCountActive(inputSquares.filter {
            it.second.xrange.first in (-50 .. 50) &&
            it.second.xrange.last in (-50 .. 50) &&
            it.second.yrange.first in (-50 .. 50) &&
            it.second.yrange.last in (-50 .. 50) &&
            it.second.zrange.first in (-50 .. 50) &&
            it.second.zrange.last in (-50 .. 50)
        })

    }

    override fun part2(): Long {
        return combineBlocksAndCountActive(inputSquares)
    }

    private fun combineBlocksAndCountActive(blocks: List<SignedBlock>): Long {
        val processedBlocks = mutableListOf<SignedBlock>()
        for (insertingBlock in blocks) {
            val newBlocks = mutableListOf<SignedBlock>()
            for (existingRec in processedBlocks) {
                // We add the opposite of everything already present that intersects the block we are adding.

                val intersectBlock = insertingBlock.second.intersection(existingRec.second) ?: continue
                newBlocks.add(SignedBlock(-existingRec.first, intersectBlock))
            }
            processedBlocks.addAll(newBlocks)
            if (insertingBlock.first == 1) processedBlocks.add(insertingBlock)
        }
        return processedBlocks.sumOf { it.first * it.second.size() }
    }
}

val day22Problem = Day22Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day22Problem.runBoth(10)
}
package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.odd
import kotlin.time.ExperimentalTime

data class Block(var fileIdx: Int, var length: Int) {
    val empty: Boolean get() = fileIdx == -1
    companion object {
        fun empty(length: Int) : Block{
            return Block(fileIdx = -1, length=length)
        }
    }
}

const val EMPTY = -1

class Day9Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2024
    override val name = "Disk Fragmenter"

    private var size: Long = 0
    private lateinit var disk: IntArray
    private lateinit var blocks: MutableList<Block>

    override fun commonParts() {
        val rawData = getInputText().filter { it.isDigit() }.map { it.toString().toLong() }
        size = rawData.sum()
        disk = IntArray(size.toInt()) { EMPTY }
        blocks = ArrayList(rawData.size)
        var diskIdx = 0
        var fileCounter = 0
        rawData.forEachIndexed { inputIdx, i ->

            if (inputIdx.odd) {
                diskIdx += i.toInt()
                blocks.add(Block.empty(length = i.toInt()))
            } else {
                blocks.add(Block(fileIdx = fileCounter, length = i.toInt()))
                repeat(i.toInt()) {
                    disk[diskIdx] = fileCounter
                    diskIdx++
                }
                fileCounter++
            }
        }
    }


    override fun part1(): Long {
        var leftmostEmptyIdx = disk.indexOfFirst { it == EMPTY }
        var rightmostNonEmptyIdx = disk.indexOfLast { it != EMPTY }
        while (leftmostEmptyIdx < rightmostNonEmptyIdx) {
            disk[leftmostEmptyIdx] = disk[rightmostNonEmptyIdx]
            disk[rightmostNonEmptyIdx] = EMPTY
            while (disk[leftmostEmptyIdx] != EMPTY) {
                leftmostEmptyIdx++
            }
            while (disk[rightmostNonEmptyIdx] == EMPTY) {
                rightmostNonEmptyIdx--
            }
        }
        return score(disk)
    }

    private fun score(diskToScore: IntArray): Long {
        return diskToScore.mapIndexed { idx, i ->
            if (i == EMPTY) 0L else idx.toLong() * i.toLong()
        }.sum()
    }


    override fun part2(): Long {
        val files = blocks.filter { !it.empty }.toMutableList()
        var blockIdx = blocks.size - 1
        repeat(files.size) {
            val fileToMove = files.removeLast()
            while (blocks[blockIdx] != fileToMove) {
                blockIdx--
            }

            // Make current position empty. It doesn't matter if we end up with consecutive empty since we have already tried to move all blocks to the right of it.
            this.blocks[blockIdx] = fileToMove.copy(fileIdx = -1)

            // find the first empty block where it fits
            val emptyBlockIdx = this.blocks.indexOfFirst { it.empty && it.length >= fileToMove.length }
            if (emptyBlockIdx != -1) {
                val emptyBlock = this.blocks[emptyBlockIdx]
                val emptySpaceLeft = emptyBlock.length - fileToMove.length
                val nextBlock = blocks[emptyBlockIdx + 1]
                if (nextBlock.empty && emptySpaceLeft > 0) {
                    nextBlock.length += emptySpaceLeft
                } else {
                    blocks[emptyBlockIdx] = fileToMove
                    if (emptySpaceLeft > 0) blocks.add(
                        emptyBlockIdx + 1,
                        Block.empty(length = emptySpaceLeft)
                    )
                }
            }  // No fitting empty block found. nop
        }
        return score(blocksToDisk(blocks))
    }

    private fun blocksToDisk(blocks: MutableList<Block>): IntArray {
        val disk = IntArray(size.toInt()) { EMPTY }
        var i = 0
        blocks.forEachIndexed { _, block ->
            repeat(block.length) {
                if (block.empty) {
                    disk[i] = EMPTY
                } else {
                    disk[i] = block.fileIdx
                }
                i++
            }
        }
        return disk
    }
}

val day9Problem = Day9Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day9Problem.runBoth(10)
}
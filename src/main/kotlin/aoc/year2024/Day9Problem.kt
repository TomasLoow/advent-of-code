package aoc.year2024

import DailyProblem
import aoc.utils.emptyMutableList
import aoc.utils.odd
import kotlin.time.ExperimentalTime

data class Block(var empty: Boolean, var fileIdx: Long?, var length: Int)

class Day9Problem : DailyProblem<Long>() {

    override val number = 9
    override val year = 2024
    override val name = "Disk Fragmenter"

    private var size: Long = 0
    private lateinit var disk: Array<Long?>
    private lateinit var blocks: MutableList<Block>
    private var numberOfFiles = 0

    override fun commonParts() {
        val rawData = getInputText().filter { it.isDigit() }.map { it.toString().toLong() }
        size = rawData.sum()
        disk = Array<Long?>(size.toInt()) { null }
        blocks = emptyMutableList<Block>()
        var diskIdx = 0
        var fileCounter = 0L
        rawData.forEachIndexed { inputIdx, i ->

            if (inputIdx.odd) {
                diskIdx += i.toInt()
                blocks.add(Block(empty = true, fileIdx = null, length = i.toInt()))
            } else {
                blocks.add(Block(empty = false, fileIdx = fileCounter, length = i.toInt()))
                repeat(i.toInt()) {
                    disk[diskIdx] = fileCounter
                    diskIdx++
                }
                fileCounter++
            }
        }
    }


    override fun part1(): Long {
        var leftmostEmptyIdx = disk.indexOfFirst { it == null }
        var rightmostNonEmptyIdx = disk.indexOfLast { it != null }
        while (leftmostEmptyIdx < rightmostNonEmptyIdx) {
            disk[leftmostEmptyIdx] = disk[rightmostNonEmptyIdx]
            disk[rightmostNonEmptyIdx] = null
            leftmostEmptyIdx = disk.indexOfFirst { it == null }
            rightmostNonEmptyIdx = disk.indexOfLast { it != null }
        }
        return score(disk)
    }

    private fun score(diskToScore: Array<Long?>): Long {
        return diskToScore.mapIndexed { idx, i ->
            if (i == null) 0L else idx * i!!
        }.sum()

    }


    override fun part2(): Long {
        val files = blocks.filter { !it.empty }.toMutableList()
        repeat(files.size) {
            var fileToMove = files.removeLast()

            val blockIdx = this.blocks.indexOf(fileToMove)
            // Make current position empty. It doesn't matter if we end up with consecutive empty since we have already tried to move all blocks to the right of it.
            this.blocks[blockIdx] = fileToMove.copy(empty = true, fileIdx = null)

            // find the first empty block where it fits
            val emptyBlockIdx = this.blocks.indexOfFirst { it.empty && it.length >= fileToMove.length }
            if (emptyBlockIdx != -1) {
                val emptyBlock = this.blocks[emptyBlockIdx]
                if (emptyBlock.length == fileToMove.length) {
                    // Simplest case, just replace
                    blocks[emptyBlockIdx] = fileToMove
                } else {
                    val emptySpaceLeft = emptyBlock.length - fileToMove.length
                    val nextBlock = blocks[emptyBlockIdx + 1]
                    if (nextBlock.empty) {
                        nextBlock.length+=emptySpaceLeft
                    } else {
                        blocks[emptyBlockIdx] = fileToMove
                        blocks.add(
                            emptyBlockIdx + 1,
                            Block(empty = true, fileIdx = fileToMove.fileIdx, length = emptySpaceLeft)
                        )
                    }
                }
            }  // No fitting empty block found. nop
        }
        return score(blocksToDisk(blocks))
    }

    private fun blocksToDisk(blocks: MutableList<Block>): Array<Long?> {
        val disk = Array<Long?>(size.toInt()) { null }
        var i = 0
        blocks.forEachIndexed { idx, block ->
            repeat(block.length) {
                if (block.empty) {
                    disk[i] = null
                } else {
                    disk[i] = block.fileIdx!!
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
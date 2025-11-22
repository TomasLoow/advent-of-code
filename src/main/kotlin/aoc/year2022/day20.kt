package aoc.year2022

import aoc.DailyProblem
import aoc.utils.parseLongLines
import kotlin.time.ExperimentalTime

class Day20Problem : DailyProblem<Long>() {

    override val number = 20
    override val year = 2022
    override val name = "Grove Positioning System"

    private lateinit var data: Array<Long>

    private fun getData() = parseLongLines(getInputText()).toTypedArray()
    private fun getIdPermutation() = (data.indices).toMutableList()

    private fun nextPermutation(p: MutableList<Int>) {
        data.forEachIndexed { idx, offset ->
            val fixedOffset = offset.mod(data.size - 1)
            val startIdx = p.indexOf(idx)

            val newIdx = startIdx + fixedOffset
            if (newIdx < data.size) {
                val x = p.removeAt(startIdx)
                p.add(newIdx, x)
            } else {
                val shiftedIdx = newIdx % (data.size - 1)
                val x = p.removeAt(startIdx)
                p.add(shiftedIdx, x)
            }
        }
    }

    private fun applyP(p: List<Int>): Array<Long> {
        return p.map { i -> data[i] }.toTypedArray()
    }

    override fun part1(): Long {
        data = getData()

        val p = getIdPermutation()
        nextPermutation(p)
        val permuted = applyP(p)
        val idxOfZero = permuted.indexOf(0)
        val v1000 = permuted[(idxOfZero + 1000) % permuted.size]
        val v2000 = permuted[(idxOfZero + 2000) % permuted.size]
        val v3000 = permuted[(idxOfZero + 3000) % permuted.size]
        return v1000 + v2000 + v3000
    }


    override fun part2(): Long {
        data = getData()
        data.forEachIndexed { idx, a -> data[idx] = a * 811589153 }

        val p = getIdPermutation()
        repeat(10) {
            nextPermutation(p)
        }
        val permuted = applyP(p)
        val idxOfZero = permuted.indexOf(0)
        return permuted[(idxOfZero + 1000) % permuted.size] + permuted[(idxOfZero + 2000) % permuted.size] + permuted[(idxOfZero + 3000) % permuted.size]
    }


}


val day20Problem = Day20Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day20Problem.testData = false
    day20Problem.runBoth(50)
}
package aoc.year2022

import DailyProblem
import aoc.utils.parseBlockList
import aoc.year2015.crappyJSON.JSON
import aoc.year2015.crappyJSON.parseJArray
import kotlin.time.ExperimentalTime


class Day13Problem : DailyProblem<Int>() {

    override val number = 13
    override val year = 2022
    override val name = "Distress Signal"

    private lateinit var data: List<Pair<JSON.A, JSON.A>>


    companion object {
        fun comparePackets(left: Any, right: Any): Int {
            if (left is JSON.I && right is JSON.I) return left.int.compareTo(right.int)
            if (left is JSON.I && right !is JSON.I) {
                return comparePackets(JSON.A(listOf(left)), right)
            }
            if (right is JSON.I) {
                return comparePackets(left, JSON.A(listOf(right)))
            }

            val l = left as JSON.A
            val r = right as JSON.A
            l.content.zip(r.content).forEach { (zl, zr) ->
                val c = comparePackets(zl, zr)
                if (c != 0) return c
            }
            return l.content.size.compareTo(r.content.size)
        }
    }


    class PacketComparator : Comparator<JSON.A> {
        override fun compare(o1: JSON.A?, o2: JSON.A?): Int {
            return comparePackets(o1!!, o2!!)
        }
    }

    override fun commonParts() {
        data =
            parseBlockList(getInputText()) { b ->
                val lines = b.lines()
                Pair(parseJArray(lines[0]).first, parseJArray(lines[1]).first)
            }
    }

    override fun part1(): Int {
        val x = data.filter { (a, b) ->
            comparePackets(a, b) != 1
        }
        return x.sumOf { data.indexOf(it) + 1 }
    }

    override fun part2(): Int {
        val allPackages = data.flatMap { (a, b) -> listOf(a, b) }
        val div1 = parseJArray("[[2]]").first
        val div2 = parseJArray("[[6]]").first
        val allPackagesWithDividers = allPackages + listOf(div1, div2)
        val sorted = allPackagesWithDividers.sortedWith(PacketComparator())
        val index1 = sorted.indexOfFirst { it == div1 } + 1
        val index2 = sorted.indexOfFirst { it == div2 } + 1
        return index1 * index2
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.runBoth(100)
}
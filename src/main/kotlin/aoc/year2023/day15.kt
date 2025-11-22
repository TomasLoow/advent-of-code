package aoc.year2023

import aoc.DailyProblem
import aoc.utils.emptyMutableList
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

private sealed interface Op {
    data class Set(val label: String, val value: Int) : Op
    data class Del(val label: String) : Op
    companion object {
        fun parse(s: String): Op {
            if ('=' in s) {
                val (label, value) = s.split("=")
                return Set(label, value.toInt())
            }
            return Del(s.dropLast(1))
        }
    }
}


class Day15Problem : DailyProblem<Int>() {
    override val number = 15
    override val year = 2023
    override val name = "Lens Library"

    private lateinit var strings: List<String>
    private lateinit var ops: List<Op>

    override fun commonParts() {
        strings = getInputText().nonEmptyLines().first().split(",")
        ops = strings.map { Op.parse(it) }
    }

    private fun hash(s: String): Int {
        return (s.fold(0) { value, c ->
            (value + c.code) * 17 % 256
        })
    }

    override fun part1(): Int {
        return strings.sumOf(::hash)
    }


    override fun part2(): Int {
        val boxes = Array<MutableList<Pair<String, Int>>>(256) { emptyMutableList() }
        val labels = mutableSetOf<String>()
        ops.forEach { op ->
            when (op) {
                is Op.Set -> {
                    labels.add(op.label)
                    val box = hash(op.label)
                    val idx = boxes[box].indexOfFirst { it.first == op.label }
                    if (idx == -1) {
                        boxes[box].add(op.label to op.value)
                    } else {
                        boxes[box].removeAt(idx)
                        boxes[box].add(idx, op.label to op.value)
                    }
                }

                is Op.Del -> {
                    labels.add(op.label)
                    val box = hash(op.label)
                    val idx = boxes[box].indexOfFirst { it.first == op.label }
                    if (idx >= 0) boxes[box].removeAt(idx)
                }
            }
        }
        return score(boxes, labels)
    }

    private fun score(boxes: Array<MutableList<Pair<String, Int>>>, labels: MutableSet<String>): Int {
        return labels.sumOf { label ->
            val box = hash(label)
            val i = boxes[box].indexOfFirst { it.first == label }
            if (i == -1) 0 else {
                val v = boxes[box][i].second
                (box + 1) * (i + 1) * v
            }
        }
    }
}


val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.runBoth(100)
}
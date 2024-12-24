package aoc.year2024

import DailyProblem
import aoc.utils.nonEmptyLines
import aoc.utils.parseTwoBlocks
import kotlin.time.ExperimentalTime

private typealias Wire = String

private sealed interface LogicGate {
    data class And(override val inpA: Wire, override val inpB: Wire, override val out: Wire) : LogicGate {
        override fun eval(a: Boolean, b: Boolean): Boolean = a && b
    }

    data class Or(override val inpA: Wire, override val inpB: Wire, override val out: Wire) : LogicGate {
        override fun eval(a: Boolean, b: Boolean): Boolean = (a || b)
    }

    data class Xor(override val inpA: Wire, override val inpB: Wire, override val out: Wire) : LogicGate {
        override fun eval(a: Boolean, b: Boolean): Boolean = (a xor b)
    }

    val inpA: Wire
    val inpB: Wire
    val out: Wire
    fun eval(a: Boolean, b: Boolean): Boolean
}

private data class FullAdder(
    val inpA: Wire,
    val inpB: Wire,
    val output: Wire,
    val carryIn: Wire,
    val carryOut: Wire,
    val and1: LogicGate.And,
    val xor1: LogicGate.Xor,
    val xor2: LogicGate.Xor,
    val and2: LogicGate.And,
    val o: LogicGate.Or
) {

}


class Day24Problem : DailyProblem<String>() {

    override val number = 24
    override val year = 2024
    override val name = "Crossed Wires"

    private lateinit var inputWires: Map<Wire, Boolean>
    private lateinit var gates: Map<Wire, LogicGate>


    override fun commonParts() {
        fun parseWires(s: String): Map<Wire, Boolean> {
            return s.nonEmptyLines().map {
                val (w, b) = it.split(": ")
                w to (if (b == "1") true else false)
            }.associate { it }
        }

        fun parseGates(s: String): Map<Wire, LogicGate> {
            return s.nonEmptyLines().associate {
                val (w1, b, w2, _, o) = it.split(" ")
                when (b) {
                    "AND" -> o to LogicGate.And(w1, w2, o)
                    "OR" -> o to LogicGate.Or(w1, w2, o)
                    "XOR" -> o to LogicGate.Xor(w1, w2, o)
                    else -> TODO()
                }
            }
        }
        parseTwoBlocks(getInputText(), ::parseWires, ::parseGates).also {
            inputWires = it.first
            gates = it.second
        }
    }

    private fun evalGates(input: Map<Wire, Boolean>, gates: Map<Wire, LogicGate>): Map<Wire, Boolean> {
        val allWires = mutableSetOf<Wire>()
        allWires.addAll(input.keys)
        gates.values.forEach {
            allWires.add(it.inpA)
            allWires.add(it.inpB)
            allWires.add(it.out)
        }
        val res = mutableMapOf<Wire, Boolean>()
        input.forEach { res[it.key] = it.value }
        while (res.keys.size < allWires.size) {
            for (wire in allWires) {
                if (wire in res.keys) continue
                val g = gates[wire]!!
                if (g.inpA in res && g.inpB in res) {
                    res[wire] = g.eval(res[g.inpA]!!, res[g.inpB]!!)
                }
            }
        }
        return res
    }

    override fun part1(): String {
        val res = evalGates(inputWires, gates)
        val q = res.toList().filter { it.first.startsWith("z") }.sortedBy { it.first }.map { it.second }.reversed()
        return toDecimalString(q).toString()
    }

    fun toDecimalString(l: List<Boolean>): Long {
        return l.fold(0L) { acc, b -> 2 * acc + if (b) 1L else 0L }
    }

    override fun part2(): String {
        for (i in (1..44)) {
            val d = i.toString().padStart(2, '0')
            println(findFullAdders("x$d", "y$d", "z$d"))
        }
        // Solved by manuallt inspecting the above. It seems that all swaps are within one broken FullAdder
        return ""
    }

    private fun findFullAdders(input1: Wire, input2: Wire, output: Wire): FullAdder? {
        val xor = gates.values.filter { it is LogicGate.Xor }
            .find { (it.inpA == input1 && it.inpB == input2) || (it.inpA == input2 && it.inpB == input1) }
        if (xor == null) return null
        val and = gates.values.filter { it is LogicGate.And }
            .find { (it.inpA == input1 && it.inpB == input2) || (it.inpA == input2 && it.inpB == input1) }
        if (and == null) return null
        val outXor = gates[output]
        if (outXor!!.inpA != xor.out && outXor.inpB != xor.out) return null
        val carryin = if (outXor.inpA == xor.out) outXor.inpB else outXor.inpA

        val and2 = gates.values.filter { it is LogicGate.And }
            .find { (it.inpA == xor.out && it.inpB == carryin) || (it.inpB == xor.out && it.inpA == carryin) }
        if (and2 == null) return null

        val o = gates.values.filter { it is LogicGate.Or }
            .find { (it.inpA == and.out && it.inpB == and2.out) || (it.inpB == and.out && it.inpA == and2.out) }
        if (o == null) return null


        return FullAdder(
            inpA = input1,
            inpB = input2,
            output = output,
            carryIn = carryin,
            carryOut = o.out,
            and1 = and as LogicGate.And,
            xor1 = xor as LogicGate.Xor,
            xor2 = outXor as LogicGate.Xor,
            and2 = and2 as LogicGate.And,
            o = o as LogicGate.Or
        )
    }
}

val day24Problem = Day24Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day24Problem.testData = false
    day24Problem.runBoth(100)
}
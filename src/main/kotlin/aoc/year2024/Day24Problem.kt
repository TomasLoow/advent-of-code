package aoc.year2024

import DailyProblem
import aoc.utils.extensionFunctions.allUnorderedPairs
import aoc.utils.extensionFunctions.nonEmptyLines
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


/*
cIn─────────────────────────────────┬─┐  ┌────────────┐
                                    │ └──►            │
                   ┌────────────┐   │    │ outXor     ├───────────────────►
                   │            │   │ ┌──►            │
   ┌───────────────► firstXor   ┼─┬─┼─┘  └────────────┘
  a┼──────┬───────►│            │ │ │
   │      │        └────────────┘ │ │
   │      │                       │ │    ┌────────────┐
   │      │                       │ └────►            │
   │      │        ┌────────────┐ │      │ andTakingC ┼─┐
   │      │        │            │ └──────►            │ │  ┌─────────┐
  b│      └────────► fistAnd    ├─────┐  └────────────┘ └──►         │
   └───────────────►            │     │                    │ Or      ┼────►
                   └────────────┘     └────────────────────►         │
                                                           └─────────┘
 */
private data class FullAdder(
    val inpA: Wire,
    val inpB: Wire,
    val output: Wire,
    val carryIn: Wire,
    val carryOut: Wire,
    val firstAnd: LogicGate.And,
    val firstXor: LogicGate.Xor,
    val xorOut: LogicGate.Xor,
    val andTakingCin: LogicGate.And,
    val orCarryOut: LogicGate.Or
)


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
                w to (b == "1")
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

    private fun toDecimalString(l: List<Boolean>): Long {
        return l.fold(0L) { acc, b -> 2 * acc + if (b) 1L else 0L }
    }

    override fun part2(): String {
        /*
        This codes makes a few assumptions. If these do not hold for all input files it will fail.

        ASSUMPTION 1. All swaps are within a single full adder. No wires need to be swapped where
        wire a and wire b are parts of different full adders.

        ASSUMPTION 2: There are never two broken full-adders adjacent to each other.
         */
        val adders = (1..44).associateWith { i ->
            val d = i.toString().padStart(2, '0')
            findFullAdder("x$d", "y$d", "z$d")
        }

        val missing = adders.filter { (_, a) -> a == null }.map { it.key }
        val fixes = missing.flatMap { i ->
            val d = i.toString().padStart(2, '0')
            fixAdder(cin = adders[i - 1]!!.carryOut, x = "x$d", y = "y$d", out = "z$d", cout = adders[i + 1]!!.carryIn)
        }
        return fixes.sorted().joinToString(",")
    }

    /**
     * Given an added that's not working, try all possible wire swaps until one makes it work.
     */
    private fun fixAdder(
        cin: Wire,
        x: Wire,
        y: Wire,
        out: Wire,
        cout: Wire
    ): List<Wire> {
        val inputWires = listOf(x, y, cin)
        val outputWires = listOf(out, cout)
        val involvedGates = mutableSetOf<LogicGate>()
        involvedGates.addAll(this.gates.values.filter { it.inpA in inputWires || it.inpB in inputWires })
        involvedGates.addAll(this.gates.values.filter { it.out in outputWires })
        val involvedInternalWires = involvedGates.map { it.out }
        involvedInternalWires.allUnorderedPairs().forEach { (a, b) ->
            val newGates = involvedGates.map {
                when (it) {
                    is LogicGate.And -> {
                        when (it.out) {
                            !in listOf(a, b) -> it.copy()
                            a -> it.copy(out = b)
                            else -> it.copy(out = a)
                        }
                    }

                    is LogicGate.Or -> {
                        when (it.out) {
                            !in listOf(a, b) -> it.copy()
                            a -> it.copy(out = b)
                            else -> it.copy(out = a)
                        }
                    }

                    is LogicGate.Xor -> {
                        when (it.out) {
                            !in listOf(a, b) -> it.copy()
                            a -> it.copy(out = b)
                            else -> it.copy(out = a)
                        }
                    }
                }
            }
            if (!newGates.any { it.inpA == it.out || it.inpB == it.out } && testIsAdder(
                    newGates,
                    inputWires,
                    outputWires
                )) return listOf(a, b)
        }
        return emptyList()
    }

    /**
     * Tests if the set of gates form a full adder.
    @param inputs a list with the labels for the x,y,carry-in wires
    @output outputs a list with the labels for the out, carry-out wires
     */
    private fun testIsAdder(
        gates: List<LogicGate>,
        inputs: List<Wire>,
        outputs: List<Wire>
    ): Boolean {
        val (a, b, cin) = inputs
        val (o, cout) = outputs
        val cases = listOf(
            listOf(false, false, false, false, false),
            listOf(false, false, true, false, true),
            listOf(false, true, false, false, true),
            listOf(true, false, false, false, true),
            listOf(false, true, true, true, false),
            listOf(true, true, false, true, false),
            listOf(true, false, true, true, false),
            listOf(true, true, true, true, true),
        )
        for (case in cases) {
            val (aval, bval, cinval, coutval, oval) = case
            try {
                val res = this.evalGates(mapOf(a to aval, b to bval, cin to cinval), gates.associateBy { it.out })
                if (res[o] != oval) {
                    return false
                }
                if (res[cout] != coutval) {
                    return false
                }
            } catch (_: Exception) {
                return false
            }
        }
        return true
    }

    private fun findFullAdder(input1: Wire, input2: Wire, output: Wire): FullAdder? {
        val firstXor = gates.values.filterIsInstance<LogicGate.Xor>()
            .find { (it.inpA == input1 && it.inpB == input2) || (it.inpA == input2 && it.inpB == input1) }
        if (firstXor == null) return null
        val firstAnd = gates.values.filterIsInstance<LogicGate.And>()
            .find { (it.inpA == input1 && it.inpB == input2) || (it.inpA == input2 && it.inpB == input1) }
        if (firstAnd == null) return null
        val outXor = gates[output]
        if (outXor!!.inpA != firstXor.out && outXor.inpB != firstXor.out) return null
        val carryIn = if (outXor.inpA == firstXor.out) outXor.inpB else outXor.inpA

        val andTakingCin = gates.values.filterIsInstance<LogicGate.And>()
            .find { (it.inpA == firstXor.out && it.inpB == carryIn) || (it.inpB == firstXor.out && it.inpA == carryIn) }
        if (andTakingCin == null) return null

        val orCarryOut = gates.values.filterIsInstance<LogicGate.Or>()
            .find { (it.inpA == firstAnd.out && it.inpB == andTakingCin.out) || (it.inpB == firstAnd.out && it.inpA == andTakingCin.out) }
        if (orCarryOut == null) return null


        return FullAdder(
            inpA = input1,
            inpB = input2,
            output = output,
            carryIn = carryIn,
            carryOut = orCarryOut.out,
            firstAnd = firstAnd as LogicGate.And,
            firstXor = firstXor as LogicGate.Xor,
            xorOut = outXor as LogicGate.Xor,
            andTakingCin = andTakingCin as LogicGate.And,
            orCarryOut = orCarryOut as LogicGate.Or
        )
    }
}

val day24Problem = Day24Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day24Problem.testData = false
    day24Problem.runBoth(1)
}
package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

private typealias Wire = String
class Day7Problem() : DailyProblem<Int>() {

    override val number = 7
    override val year = 2015
    override val name = "Some Assembly Required"

    private lateinit var circuit: Map<Wire, Gate>
    private lateinit var cache: MutableMap<Wire, Int>
    private var valuePart1: Int? = null


    sealed class Gate {}
    class Const(val value: Int): Gate()
    class And(val inpA: Wire, val inpB: Wire): Gate()
    class Or(val inpA: Wire, val inpB: Wire): Gate()
    class LShift(val inp: Wire, val amount: Int): Gate()
    class RShift(val inp: Wire, val amount: Int): Gate()
    class Not(val inp: Wire): Gate()
    class Nop(val inp: Wire): Gate()

    fun parseGate(s: String): Gate {
        if (s.all { c -> c.isDigit()}) return Const(s.toInt())
        if (' ' !in s ) return Nop(s)
        if (s.startsWith("NOT ")) return Not(s.substringAfter("NOT "))
        val (a, op, b) = s.split(" ")
        when(op) {
            "AND" -> return And(a,b)
            "OR" -> return Or(a,b)
            "LSHIFT" -> return LShift(a, b.toInt())
            "RSHIFT" -> return RShift(a, b.toInt())
        }
        throw Exception("Parse error")
    }

    override fun commonParts() {

        circuit = parseListOfPairs(getInputText(), ::parseGate, { it }, separator = " -> ").map { it.flip() }.toMap()
        cache = mutableMapOf<Wire, Int>()
    }


    fun evaluate(circuit: Map<Wire,Gate>, wire: Wire): Int {
        if (wire in cache) return cache[wire]!!
        if (wire.all { c -> c.isDigit()}) return wire.toInt()
        val gate = circuit[wire]!!
        val value = when(gate) {
            is Const ->  gate.value
            is And -> evaluate(circuit, gate.inpA) and evaluate(circuit, gate.inpB)
            is Or -> evaluate(circuit, gate.inpA) or evaluate(circuit, gate.inpB)
            is LShift -> evaluate(circuit, gate.inp) shl gate.amount
            is RShift -> evaluate(circuit, gate.inp) shr gate.amount
            is Nop -> evaluate(circuit, gate.inp)
            is Not -> evaluate(circuit, gate.inp).inv()
        }
        cache[wire] = value
        return value
    }

    override fun part1(): Int {
        cache.clear()
        valuePart1 = evaluate(circuit,"a")
        return valuePart1!!
    }


    override fun part2(): Int {
        if (valuePart1 != null) part1()  // Answer from part1 needed for part2
        cache.clear()

        /* Change the value of b in the circuit to valuePart1 */
        val moddedCircuit = circuit.keys.associate { key ->
            if (key == "b") {
                Pair(key, Const(valuePart1!!))
            } else {
                Pair(key, circuit[key]!!)
            }
        }
        return evaluate(moddedCircuit, "a")
    }
}


val day7Problem = Day7Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.runBoth(100)
}
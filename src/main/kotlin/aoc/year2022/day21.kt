package aoc.year2022

import aoc.DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.id
import aoc.utils.parseListOfPairs
import kotlin.time.ExperimentalTime


enum class MathOp {
    ADD, SUB, MUL,DIV
}

fun reverseMathOp(a: Long, op: MathOp, res: Long, unknonwOnRight:Boolean=true) : Long {
    return when(op) {
        MathOp.ADD -> res - a
        MathOp.SUB -> if (unknonwOnRight) a -res else res + a
        MathOp.MUL -> res / a
        MathOp.DIV -> if (unknonwOnRight) a / res else res*a
    }
}

sealed interface Monkey {
    class Num(val value: Long) : Monkey
    class Op(val op: MathOp, val opfun: (Long, Long) -> Long, val m1: String, val m2: String) : Monkey
}

class Day21Problem : DailyProblem<Long>() {

    override val number = 21
    override val year = 2022
    override val name = "Monkey Math"


    private lateinit var inputMonkeys: Map<String, Monkey>

    override fun commonParts() {
        fun parseMonkey(s: String): Monkey {
            return if (s.first().isDigit()) {
                Monkey.Num(s.toLong())
            } else {
                val (m1, oprator, m2) = s.split(" ")
                val opfun: (Long, Long) -> Long = when (oprator) {
                    "+" -> { a: Long, b: Long -> a + b }
                    "-" -> { a: Long, b: Long -> a - b }
                    "*" -> { a: Long, b: Long -> a * b }
                    "/" -> { a: Long, b: Long -> a / b }
                    else -> throw Exception()
                }
                val op: MathOp = when (oprator) {
                    "+" -> MathOp.ADD
                    "-" -> MathOp.SUB
                    "*" -> MathOp.MUL
                    "/" -> MathOp.DIV
                    else -> throw Exception()
                }
                Monkey.Op(op, opfun, m1, m2)
            }
        }
        inputMonkeys = parseListOfPairs(getInputText(), ::id, ::parseMonkey, ": ").toMap()

    }

    private val cache = emptyMutableMap<String, Long>()
    private fun calculateSubtree(name: String, data: Map<String, Monkey>): Long {
        if (name in cache) return cache[name]!!
        val res = when (val m = data[name]!!) {
            is Monkey.Num -> m.value
            is Monkey.Op -> {
                val v1 = calculateSubtree(m.m1, data)
                val v2 = calculateSubtree(m.m2, data)
                m.opfun(v1, v2)
            }
        }
        cache[name] = res
        return res
    }

    override fun part1(): Long {
        cache.clear()
        return calculateSubtree("root", inputMonkeys)
    }


    override fun part2(): Long {
        cache.clear()
        val modded = inputMonkeys.toMutableMap()
        modded.remove("humn")
        val root = modded["root"]!! as Monkey.Op

        /* Find out which subtree has full data and which contains a silly humn */
        var unknownRoot: String
        var target: Long
        try {
            target = calculateSubtree(root.m2, modded)
            unknownRoot = root.m1
        } catch (e: Exception) {
            target = calculateSubtree(root.m1, modded)
            unknownRoot = root.m2
        }

        return findVariableValueInSubtree("humn", target, unknownRoot, modded)
    }

    private fun findVariableValueInSubtree(variableName: String, target: Long, rootName: String, monkees: Map<String, Monkey>): Long{
        if (variableName == rootName) return target
        val rootMonkey = monkees[rootName] as Monkey.Op

        try {
            calculateSubtree(rootMonkey.m1, monkees)
        } catch (_: Exception) {} // excpect failure. We just want to fill up the cache.
        try {
            calculateSubtree(rootMonkey.m2, monkees)
        } catch (_: Exception) {} // excpect failure. We just want to fill up the cache.

        val newTarget: Long
        val unknownMonkey: String
        if (rootMonkey.m1 in cache) {
            val valueOfKnownSubtree = cache[rootMonkey.m1]!!
            unknownMonkey = rootMonkey.m2
            newTarget = reverseMathOp(valueOfKnownSubtree, rootMonkey.op, target, unknonwOnRight = true)
        } else {
            val valueOfKnownSubtree = cache[rootMonkey.m2]!!
            unknownMonkey = rootMonkey.m1
            newTarget = reverseMathOp(valueOfKnownSubtree, rootMonkey.op, target, unknonwOnRight = false)
        }
        return findVariableValueInSubtree(variableName, newTarget, unknownMonkey, monkees)
    }
}


val day21Problem = Day21Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day21Problem.testData = false
    day21Problem.runBoth(100)
}
package aoc.year2022

import DailyProblem
import aoc.utils.lcm
import aoc.utils.parseBlockList
import aoc.utils.product
import kotlin.time.ExperimentalTime

class Day11Problem() : DailyProblem<Long>() {

    override val number = 11
    override val year = 2022
    override val name = "Monkey in the Middle"

    private lateinit var state: Map<Int, Monkey>

    fun parseMonkeys(): Map<Int, Monkey> {
        return parseBlockList(getInputText(), ::parseMonkey).mapIndexed { idx, monkeyRule -> Pair(idx, monkeyRule) }
            .toMap()
    }

    private fun parseMonkey(s: String): Monkey {
        val sLines = s.lines()
        val lineName = sLines.first()
        val (lineStart, lineOp, lineTest, lineTrue, lineFalse) = sLines.drop(1)
        val name = lineName.substringAfter("Monkey ").dropLast(1).toInt()
        val op = if ("+" in lineOp) {
            MonkeyOpAdd(lineOp.substringAfter(" + ").toInt())
        } else {
            if ("old * old" in lineOp) MonkeyOpSquared(0) else MonkeyOpMul(lineOp.substringAfter(" * ").toInt())
        }
        val startingItems =
            lineStart.substringAfter("  Starting items: ").split(", ").map { it.toLong() }.toMutableList()
        val test = lineTest.substringAfter("  Test: divisible by ").toInt()
        val ttt = lineTrue.substringAfter("If true: throw to monkey ").toInt()
        val ttf = lineFalse.substringAfter("If false: throw to monkey ").toInt()
        return Monkey(name, startingItems, op, test, ttt, ttf)
    }

    data class Monkey(
        val number: Int,
        val heldItems: MutableList<Long>,
        val operation: MonkeyOp,
        val test: Int,
        val throwToTrue: Int,
        val throwToFalse: Int,
        var actions: Long = 0L
    ) {

    }

    fun applyOp(op: MonkeyOp, v: Long): Long = when (op) {
        is MonkeyOpAdd -> v + op.value
        is MonkeyOpMul -> v * op.value
        is MonkeyOpSquared -> v * v
    }

    sealed class MonkeyOp(internal val value: Int)
    class MonkeyOpAdd(value: Int) : MonkeyOp(value)
    class MonkeyOpMul(value: Int) : MonkeyOp(value)
    class MonkeyOpSquared(_value: Int) : MonkeyOp(_value)

    fun monkeyStep(state: Map<Int, Monkey>, divisor: Int = 3, modulus: Int? = null) {
        state.forEach { i, m ->
            m.heldItems.forEach { currentItem ->
                var item = currentItem
                item = applyOp(m.operation, item)
                item /= divisor
                if (modulus != null && item > modulus) {
                    item %= modulus
                }
                m.actions++
                if (item % m.test == 0L) {
                    state[m.throwToTrue]!!.heldItems.add(item)
                } else {
                    state[m.throwToFalse]!!.heldItems.add(item)
                }
            }
            m.heldItems.clear()
        }
    }

    override fun part1(): Long {
        state = parseMonkeys()

        repeat(20) { i ->
            monkeyStep(state)
        }
        return state.values.map { m -> m.actions }.sortedDescending().take(2).product()
    }


    override fun part2(): Long {
        state = parseMonkeys()


        // To keep item values sane, we calculate them modulo the lcm of all test values.
        // This doesn't affect any operations or tests.
        // Mine were all prime so a simple product would have worked in hindsight, but oh well. 20/20
        val modulus = state.values.map { it.test }.fold(1) { l, x -> lcm(l, x) }

        repeat(10000) { i ->
            monkeyStep(state, 1, modulus = modulus)
        }
        return state.values.map { m -> m.actions }.sortedDescending().take(2).product()
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    println(day11Problem.runBoth(100))
}
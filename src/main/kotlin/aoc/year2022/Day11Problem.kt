package aoc.year2022

import DailyProblem
import aoc.utils.lcm
import aoc.utils.parseBlockList
import aoc.utils.extensionFunctions.product
import kotlin.time.ExperimentalTime


class Day11Problem : DailyProblem<Long>() {

    override val number = 11
    override val year = 2022
    override val name = "Monkey in the Middle"

    private lateinit var state: Map<Int, Monkey>

    fun parseMonkeys(): Map<Int, Monkey> {
        val monkeys =
            parseBlockList(getInputText(), ::parseMonkey).mapIndexed { idx, monkeyRule -> Pair(idx, monkeyRule) }
                .toMap()
        monkeys.forEach { idx, monkey ->
            monkey.throwToTrue = monkeys[monkey.throwToTrueIdx]!!
            monkey.throwToFalse = monkeys[monkey.throwToFalseIdx]!!
        }
        return monkeys
    }

    private fun parseMonkey(s: String): Monkey {
        val sLines = s.lines()
        val lineName = sLines.first()
        val (lineStart, lineOp, lineTest, lineTrue, lineFalse) = sLines.drop(1)
        val name = lineName.substringAfter("Monkey ").dropLast(1).toInt()
        val op = if ("+" in lineOp) {
            MonkeyOp.Add(lineOp.substringAfter(" + ").toInt())
        } else {
            if ("old * old" in lineOp) MonkeyOp.Squared() else MonkeyOp.Mul(lineOp.substringAfter(" * ").toInt())
        }
        val startingItems =
            lineStart.substringAfter("  Starting items: ").split(", ").map { it.toLong() }.toMutableList()
        val test = lineTest.substringAfter("  Test: divisible by ").toInt()
        val ttt = lineTrue.substringAfter("If true: throw to monkey ").toInt()
        val ttf = lineFalse.substringAfter("If false: throw to monkey ").toInt()
        return Monkey(name, ArrayDeque(startingItems), op, test, ttt, ttf)
    }

    data class Monkey(
        val number: Int,
        val heldItems: ArrayDeque<Long>,
        val operation: MonkeyOp,
        val test: Int,
        val throwToTrueIdx: Int,
        val throwToFalseIdx: Int,
        var actions: Long = 0L
    ) {

        var throwToTrue: Monkey? = null
        var throwToFalse: Monkey? = null
    }

    fun applyOp(op: MonkeyOp, v: Long): Long = when (op) {
        is MonkeyOp.Add -> v + op.value
        is MonkeyOp.Mul -> v * op.value
        is MonkeyOp.Squared -> v * v
    }

    sealed interface MonkeyOp {
        class Add(val value: Int) : MonkeyOp
        class Mul(val value: Int) : MonkeyOp
        class Squared : MonkeyOp
    }

    fun monkeyStep(state: Map<Int, Monkey>, divisor: Int = 3) {
        // To keep item values  for step 2, we calculate them modulo the lcm of all test values.
        // This doesn't affect any operations or tests.
        // Mine were all prime so a simple product would have worked in hindsight, but oh well. 20/20
        val modulus = state.values.map { it.test }.fold(1) { l, x -> lcm(l, x) }

        state.forEach { i, m ->
            while(m.heldItems.isNotEmpty()) {
                var item = m.heldItems.removeFirst()
                item = applyOp(m.operation, item)
                item /= divisor
                item %= modulus
                m.actions++
                if (item % m.test == 0L) {
                    m.throwToTrue!!.heldItems.addLast(item)
                } else {
                    m.throwToFalse!!.heldItems.addLast(item)
                }
            }
            m.heldItems.clear()
        }
    }

    override fun part1(): Long {
        state = parseMonkeys()
        repeat(20) { i ->
            monkeyStep(state, divisor = 3)
        }
        return state.values.map { m -> m.actions }.sortedDescending().take(2).product()
    }

    override fun part2(): Long {
        state = parseMonkeys()
        repeat(10000) { i ->
            monkeyStep(state, 1)
        }
        return state.values.map { m -> m.actions }.sortedDescending().take(2).product()
    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    println(day11Problem.runBoth(100))
}
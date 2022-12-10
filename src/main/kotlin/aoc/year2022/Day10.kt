package aoc.year2022

import DailyProblem
import aoc.utils.Array2D
import aoc.utils.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day10Problem() : DailyProblem<Int?>() {

    override val number = 10
    override val year = 2022
    override val name = "Problem name"

    private lateinit var signal: Map<Int, Int>
    lateinit var output: String

    sealed class Operator()
    class Noop : Operator()
    class Addx(val value: Int) : Operator()

    override fun commonParts() {
        val operators = parseOperators(getInputText())
        this.signal = calculateFullSignal(operators)
    }

    private fun parseOperators(s: String) = s.nonEmptyLines().map { line ->
        if (line == "noop") {
            Noop()
        } else {
            Addx(line.substringAfter("addx ").toInt())
        }
    }

    private fun calculateFullSignal(data: List<Operator>): Map<Int, Int> {
        var step = 1
        var signal = 1

        val fullSignal = buildMap<Int, Int> {
            this[1] = 1
            data.forEach {
                when (it) {
                    is Addx -> {
                        step += 1
                        this[step] = signal
                        step += 1
                        signal += it.value
                        this[step] = signal
                    }

                    is Noop -> {
                        step += 1
                        this[step] = signal
                    }
                }
            }
        }
        return fullSignal
    }

    override fun part1(): Int {
        return (0..12).map { 20 + it * 40 }.filter { it < signal.size }.sumOf { i ->
            i * signal[i]!!
        }
    }

    override fun part2(): Int? {
        val screenData = ArrayList<Boolean>(40 * 6)
        repeat(40 * 6) { screenData.add(false) }


        (1..40 * 6 + 1).forEach { t ->
            val screenX = (t-1) % 40
            val spriteX = signal[t]!!
            if (screenX in listOf(spriteX - 1, spriteX, spriteX + 1)) {
                screenData[t - 1] = true
            }
        }
        val screen: Array2D<Boolean> = Array2D(screenData, 40, 6)
        this.output = screen.show { Array2D.a2renderBool(it) }
        return null
    }
}

val day10Problem = Day10Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.runBoth(1)
    print(day10Problem.output)
}
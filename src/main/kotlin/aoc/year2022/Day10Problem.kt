package aoc.year2022

import DailyProblem
import aoc.utils.geometry.Array2D
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.showBooleanArray
import aoc.utils.parseDisplay
import kotlin.time.ExperimentalTime

class Day10Problem : DailyProblem<Any>() {

    override val number = 10
    override val year = 2022
    override val name = "Cathode-Ray Tube"

    private lateinit var signal: Map<Int, Int>
    lateinit var output: String

    sealed interface Op {
        data object Noop : Op
        class Addx(val value: Int) : Op
    }

    override fun commonParts() {
        val operators = parseOperators(getInputText())
        this.signal = calculateFullSignal(operators)
    }

    private fun parseOperators(s: String) = s.nonEmptyLines().map { line ->
        if (line == "noop") Op.Noop else Op.Addx(line.substringAfter("addx ").toInt())
    }

    private fun calculateFullSignal(data: List<Op>): Map<Int, Int> {
        var step = 1
        var signal = 1

        val fullSignal = buildMap {
            this[1] = 1
            data.forEach {
                when (it) {
                    is Op.Addx -> {
                        step += 1
                        this[step] = signal
                        step += 1
                        signal += it.value
                        this[step] = signal
                    }

                    is Op.Noop -> {
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

    override fun part2(): String {
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
        this.output = showBooleanArray(screen)
        return parseDisplay(output)
    }
}

val day10Problem = Day10Problem()



@OptIn(ExperimentalTime::class)
fun main() {
    day10Problem.runBoth(1)
    print(day10Problem.output)
    println(parseDisplay(day10Problem.output))
}


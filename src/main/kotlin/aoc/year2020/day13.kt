package aoc.year2020

import aoc.DailyProblem
import aoc.utils.math.chineseRemainder
import aoc.utils.extensionFunctions.nonEmptyLines
import java.math.BigInteger
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime


class Day13Problem : DailyProblem<BigInteger>() {

    override val number = 13
    override val year = 2020
    override val name = "Shuttle Search"

    private var time by Delegates.notNull<BigInteger>()
    lateinit var lines: List<BigInteger?>

    override fun commonParts() {
        getInputText().nonEmptyLines().let { lines ->
            time = lines.first().toBigInteger()
            this.lines = lines.drop(1).first().split(",").map { if (it == "x") null else it.toBigInteger() }
        }
    }


    override fun part1(): BigInteger {
        val line = lines.filterNotNull().minByOrNull { (BigInteger.ONE + time / it) * it - time }!!

        return line * ((BigInteger.ONE + time / line) * line - time)
    }


    override fun part2(): BigInteger {
        val equations = lines
            .withIndex()
            .filter { it.value != null }
            .map { it.value!! to it.value!! - (it.index).toBigInteger() }
        val x = chineseRemainder(equations)
        return x
    }
}

val day13Problem = Day13Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day13Problem.testData = false
    day13Problem.runBoth(1)
}
package aoc.year2020

import DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

class Day19Problem : DailyProblem<Int>() {

    override val number = 19
    override val year = 2020
    override val name = "Monster Messages"

    private lateinit var rules: Array<String>
    private lateinit var strings: List<String>
    private lateinit var regexes: Array<Regex?>

    override fun commonParts() {
        fun parseRules(s: String): Array<String> {
            val lines = s.nonEmptyLines()
            val m = lines.maxOf { it.split(": ").first().toInt() }
            val a = Array(m + 1) { "" }
            lines.forEach {
                val (i, r) = it.split(": ")
                a[i.toInt()] = r
            }
            return a
        }
        val (r, s) = parseTwoBlocks(getInputText(), ::parseRules, { it.nonEmptyLines() })
        rules = r
        strings = s
        regexes = Array(r.size) { null }
    }

    fun getRegex(i: Int): Regex {
        if (regexes[i] != null) { return regexes[i]!!}
        val ruleString = rules[i]!!
        val r = if (ruleString.contains("\"")) Regex(ruleString.replace("\"", ""))
        else if (ruleString.contains("|")) {
            val (a, b) = ruleString.split(" | ")
            val aRegs = a.split(" ").map { getRegex(it.toInt()) }
            val aRex = aRegs.joinToString("") { "(" + it + ")" }
            val bRegs = b.split(" ").map {
                val r = getRegex(it.toInt())
                if (it.toInt() == i) {
                    Regex("(" + r.pattern + ")*")
                } else r
            }
            val bRex = bRegs.joinToString("") { "(" + it + ")" }

            Regex("($aRex)|($bRex)")
        } else {
            val regs = ruleString.split(" ").map { getRegex(it.toInt()) }
            Regex(regs.joinToString("") { "(" + it.pattern + ")" })
        }
        regexes[i] = r
        return r
    }

    override fun part1(): Int {
        for (i in (0..rules.size -1)) {
            if (rules[i].isNotEmpty()) println("$i: ${getRegex(i)}")
        }

        val re0 = getRegex(0)
        return strings.count {
            re0.matches(it)
        }
    }

    override fun part2(): Int {
        return 1
    }
}

val day19Problem = Day19Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day19Problem.testData = true
    day19Problem.runBoth(1)
}
package aoc.year2020

import aoc.DailyProblem
import aoc.utils.*
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.time.ExperimentalTime

sealed interface P {
    fun match(s: String, rulesMap: (Int) -> P): List<Int>

    data class Constant(val c: Char) : P {
        override fun match(s: String, rulesMap: (Int) -> P): List<Int> {
            if (s.isEmpty()) return emptyList()
            return if (s.first() == c) listOf(1)
            else emptyList()
        }
    }

    data class Concat(val rules: List<Int>) : P {
        override fun match(s: String, rulesMap: (Int) -> P): List<Int> {
            if (rules.size == 1) return rulesMap(rules.first()).match(s, rulesMap)
            return rulesMap(rules.first()).match(s, rulesMap).flatMap { consumed ->
                Concat(rules.drop(1)).match(s.drop(consumed), rulesMap).map { it + consumed }
            }
        }
    }

    data class Or(val rules1: List<Int>, val rules2: List<Int>) : P {
        override fun match(s: String, rulesMap: (Int) -> P): List<Int> {
            val firstMatches = Concat(rules1).match(s, rulesMap)
            val secondMatches = Concat(rules2).match(s, rulesMap)
            return (firstMatches + secondMatches).distinct()
        }
    }

    data class Plus(val rule: Int) : P {
        override fun match(s: String, rulesMap: (Int) -> P): List<Int> {
            val r = rulesMap(rule)
            val consumedSingleMatch = r.match(s, rulesMap)
            if (consumedSingleMatch.isEmpty()) return consumedSingleMatch
            return buildList {
                addAll(consumedSingleMatch)
                consumedSingleMatch.forEach { c ->
                    addAll(Plus(rule).match(s.drop(c), rulesMap).map { it + c })
                }
            }
        }
    }

    data class Matching(val rule1: Int, val rule2: Int) : P {
        override fun match(s: String, rulesMap: (Int) -> P): List<Int> {
            val consumed1 = Concat(listOf(rule1, rule2)).match(s, rulesMap)
            // if (consumed1.isEmpty()) return emptyList()

            val r1 = rulesMap(rule1)
            val r2 = rulesMap(rule2)
            val ate1 = r1.match(s, rulesMap)
            val ate1plusM = ate1.flatMap { c ->
                this.match(s.drop(c), rulesMap).map { it + c }
            }
            val ate1plusMplus2 = ate1plusM.flatMap { c ->
                r2.match(s.drop(c), rulesMap).map { it + c }
            }
            return (ate1plusMplus2 + consumed1).distinct()
        }


    }
}

class Day19Problem : DailyProblem<Int>() {

    override val number = 19
    override val year = 2020
    override val name = "Monster Messages"

    private lateinit var rules: Array<String>
    private lateinit var strings: List<String>
    private lateinit var parsers: Array<P?>

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
        val (r, s) = parseTwoBlocks(getInputText(), ::parseRules) { it.nonEmptyLines() }
        rules = r
        strings = s
        parsers = Array(r.size) { null }
    }

    fun getParser(i: Int): P {
        if (parsers[i] != null) {
            return parsers[i]!!
        }
        val ruleString = rules[i]


        val r = if (ruleString.contains("\"")) P.Constant(ruleString[1])
        else if (ruleString.contains("|")) {
            val (a, b) = ruleString.split(" | ")
            val aRegs = a.split(" ").map { it.toInt() }
            val bRegs = b.split(" ").map { it.toInt() }
            when (i) {
                bRegs.last() -> P.Plus(aRegs.first()) // case X -> Y | Y X
                in bRegs -> P.Matching(bRegs.first(), bRegs.last()) // case X -> L R | L X R
                else -> P.Or(aRegs, bRegs)
            }
        } else {
            val regs = ruleString.split(" ").map { it.toInt() }
            P.Concat(regs)
        }
        parsers[i] = r
        return r
    }

    override fun part1(): Int {
        parsers.indices.forEach { i -> parsers[i] = null }
        rules[8] = "42"
        rules[11] = "42 31"

        val p0 = getParser(0)
        return strings.count { str ->
            val i = p0.match(str, ::getParser)
            i.any { consumed -> consumed == str.length }
        }
    }

    override fun part2(): Int {
        parsers.indices.forEach { i -> parsers[i] = null }
        rules[8] = "42 | 42 8"
        rules[11] = "42 31 | 42 11 31"

        val p0 = getParser(0)
        return strings.count { str ->
            val i = p0.match(str, ::getParser)
            i.any { consumed -> consumed == str.length }
        }
    }
}

val day19Problem = Day19Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day19Problem.testData = true
    day19Problem.runBoth(100)
}
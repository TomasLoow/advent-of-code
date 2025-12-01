package aoc

import aoc.utils.emptyMutableMap
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

val events = mapOf(
    2025 to aoc.year2025.solvedProblems,
    2024 to aoc.year2024.solvedProblems,
    2023 to aoc.year2023.solvedProblems,
    2022 to aoc.year2022.solvedProblems,
    2021 to aoc.year2021.solvedProblems,
    2020 to aoc.year2020.solvedProblems,
    2019 to aoc.year2019.solvedProblems,
    2017 to aoc.year2017.solvedProblems,
    2015 to aoc.year2015.solvedProblems
)
val RUN_FOR_YEAR: Int? = null
const val TIMES_TO_RUN = 10

@OptIn(ExperimentalTime::class)
fun main() {
    val yearSummary = emptyMutableMap<Int, Duration>()
    val eventsToRun = if (RUN_FOR_YEAR == null) {
        events
    } else {
        mapOf(RUN_FOR_YEAR to events[RUN_FOR_YEAR]!!)
    }
    eventsToRun.forEach { (year, problems) ->
        val durations = problems.associate { problem ->
            val dur = problem.runBoth(timesToRun = TIMES_TO_RUN)
            Pair(problem.number, dur)
        }
        println("=== Timing summary ===")
        durations.forEach {
            val micros = it.value.inWholeMicroseconds.toString().padStart(10)
            println("Day ${it.key.toString().padStart(2)}\t$micros us")        }
        val totalTime = durations.values.reduce { acc, duration -> acc + duration }
        println("---------------------")
        val micros = totalTime.inWholeMicroseconds.toString().padStart(10)
        println("Total\t$micros us")
        println("(${(totalTime.inWholeMilliseconds)/1000.0}s)")
        println()
        val slowest = durations.toList().maxByOrNull { (_, dur) -> dur }!!
        println("Slowest problem: ${slowest.first}: ${slowest.second.inWholeMicroseconds} us")
        println()

        yearSummary[year] = totalTime
    }
    println("=== Year totals ===")
    yearSummary.forEach { (year, dur) ->
        val numProblems = eventsToRun[year]!!.size
        val avg = dur / numProblems
        println("Year ${year}\t${dur.inWholeMicroseconds} us\t($numProblems solved)\t$avg/problem")
    }
}
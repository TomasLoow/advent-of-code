package aoc

import aoc.utils.emptyMutableMap
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

val events = mapOf(
    2024 to listOf(
        aoc.year2024.day1Problem,
        aoc.year2024.day2Problem,
        aoc.year2024.day3Problem,
        aoc.year2024.day4Problem,
        aoc.year2024.day5Problem,
        aoc.year2024.day6Problem,
        aoc.year2024.day7Problem,
        aoc.year2024.day8Problem,
        aoc.year2024.day9Problem,
        aoc.year2024.day10Problem,
        aoc.year2024.day11Problem,
        aoc.year2024.day12Problem,
        aoc.year2024.day13Problem,
        aoc.year2024.day14Problem,
        aoc.year2024.day15Problem,
        aoc.year2024.day16Problem,  //Slooooowwww.... :(
        aoc.year2024.day17Problem,
        aoc.year2024.day18Problem,
        aoc.year2024.day19Problem,
        aoc.year2024.day20Problem,
        aoc.year2024.day21Problem,
        aoc.year2024.day22Problem,
        aoc.year2024.day23Problem,
        aoc.year2024.day24Problem,
        aoc.year2024.day25Problem,
    ),
    2023 to listOf(
        aoc.year2023.day1Problem,
        aoc.year2023.day2Problem,
    ),
    2022 to listOf(
        aoc.year2022.day1Problem,
        aoc.year2022.day2Problem,
        aoc.year2022.day3Problem,
        aoc.year2022.day4Problem,
        aoc.year2022.day5Problem,
        aoc.year2022.day6Problem,
        aoc.year2022.day7Problem,
        aoc.year2022.day8Problem,
        aoc.year2022.day9Problem,
        aoc.year2022.day10Problem,
        aoc.year2022.day11Problem,
        aoc.year2022.day12Problem,
        aoc.year2022.day13Problem,
        aoc.year2022.day14Problem,
        aoc.year2022.day15Problem,
        //aoc.year2022.day16Problem, // Yikes, lets exclude this one, it takes more than 1 minute! :(
        aoc.year2022.day17Problem,
        aoc.year2022.day18Problem,

        aoc.year2022.day20Problem,
        aoc.year2022.day21Problem,
        aoc.year2022.day25Problem,

        ),
    2021 to listOf(
        aoc.year2021.day1Problem,
        aoc.year2021.day2Problem,
        aoc.year2021.day3Problem,
        aoc.year2021.day4Problem,
        aoc.year2021.day5Problem,
        aoc.year2021.day6Problem,
        aoc.year2021.day7Problem,
        aoc.year2021.day8Problem,
        aoc.year2021.day9Problem,
        aoc.year2021.day10Problem,
        aoc.year2021.day11Problem,
        aoc.year2021.day12Problem,
        aoc.year2021.day13Problem,
        aoc.year2021.day14Problem,
        aoc.year2021.day15Problem,
        aoc.year2021.day16Problem,
        aoc.year2021.day17Problem,
        aoc.year2021.day18Problem,
        aoc.year2021.day21Problem,
        aoc.year2021.day22Problem,
        aoc.year2021.day25Problem,
    ),
    2019 to listOf(
        aoc.year2019.day1Problem,
        aoc.year2019.day2Problem,
        aoc.year2019.day3Problem,
        aoc.year2019.day4Problem,
    ),
    2020 to listOf(
        aoc.year2020.day1Problem,
        aoc.year2020.day2Problem,
        aoc.year2020.day24Problem
    ),
    2015 to listOf(
        aoc.year2015.day1Problem,
        aoc.year2015.day2Problem,
        aoc.year2015.day3Problem,
        aoc.year2015.day4Problem, //Slow as heck (> 1s)
        aoc.year2015.day5Problem,
        aoc.year2015.day6Problem,
        aoc.year2015.day7Problem,
        aoc.year2015.day8Problem,
        aoc.year2015.day9Problem, //Improvement potential, brute force
        aoc.year2015.day10Problem,
        aoc.year2015.day11Problem, //Improvement potential, brute force
        aoc.year2015.day12Problem,
        aoc.year2015.day13Problem, //Improvement potential, brute force
        aoc.year2015.day14Problem,
        aoc.year2015.day15Problem,
        aoc.year2015.day16Problem,
        aoc.year2015.day17Problem,
        aoc.year2015.day18Problem,
        aoc.year2015.day19Problem,
        aoc.year2015.day20Problem,
        aoc.year2015.day21Problem,
        aoc.year2015.day23Problem,
        aoc.year2015.day25Problem,

        )
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
            println("Day ${it.key}\t${it.value}")
        }
        val totalTime = durations.values.reduce { acc, duration -> acc + duration }
        println("Total time for $year: $totalTime")
        println()
        val slowest = durations.toList().maxByOrNull { (_, dur) -> dur }!!
        println("Slowest problem: ${slowest.first}: ${slowest.second}")
        println()

        yearSummary[year] = totalTime
    }
    println("=== Year totals ===")
    yearSummary.forEach { (year, dur) ->
        val numProblems = eventsToRun[year]!!.size
        val avg = dur / numProblems
        println("Year ${year}\t${dur}\t($numProblems solved)\t$avg/problem")
    }
}
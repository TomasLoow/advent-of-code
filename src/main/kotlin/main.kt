import kotlin.time.Duration
import kotlin.time.ExperimentalTime

val events = mapOf(
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
        )
)
val RUN_FOR_YEAR: Int? = null
val TIMES_TO_RUN = 50

@OptIn(ExperimentalTime::class)
fun main() {
    val yearSummary = mutableMapOf<Int, Duration>()
    val eventsToRun = if (RUN_FOR_YEAR == null) {
        events
    } else {
        events.filter { (k, _) -> k == RUN_FOR_YEAR }
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
        yearSummary[year] = totalTime
    }
    println("=== Year totals ===")
    yearSummary.forEach { year, dur ->
        println("Year ${year}\t${dur}")
    }
}
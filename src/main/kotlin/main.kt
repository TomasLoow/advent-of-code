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
    ),
    2015 to listOf(
        aoc.year2015.day1Problem,
        aoc.year2015.day2Problem,
    )
)

@OptIn(ExperimentalTime::class)
fun main() {
    val yearSummary = mutableMapOf<Int, Duration>()
    events.forEach { (year, problems) ->
        val durations = problems.associate { problem ->
            val dur = problem.runBoth(timesToRun = 1000)
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
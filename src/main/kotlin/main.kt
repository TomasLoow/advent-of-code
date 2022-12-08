import aoc.year2022.*
import kotlin.time.ExperimentalTime

val events = mapOf(
    2022 to listOf(
        day1Problem,
        day2Problem,
        day3Problem,
        day4Problem,
        day5Problem,
        day6Problem,
        day7Problem,
        day8Problem,
    )
)

@OptIn(ExperimentalTime::class)
fun main() {
    events.forEach { (year, problems) ->
        val durations = problems.associate { problem ->
            val dur = problem.runBoth(timesToRun = 1000)
            Pair(problem.number, dur)
        }
        println("=== Timing summary ===")
        durations.forEach {
            println("Day ${it.key}\t${it.value}")
        }
        println("Total time for $year: ${durations.values.reduce { acc, duration -> acc + duration }}")
        println()
    }
}
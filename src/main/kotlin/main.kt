import kotlin.time.ExperimentalTime

val problems = listOf(
    aoc2022.day1Problem,
    aoc2022.day2Problem,
    aoc2022.day3Problem,
    aoc2022.day4Problem,
    aoc2022.day5Problem,
    aoc2022.day6Problem,
    aoc2022.day7Problem,
    aoc2022.day8Problem,
)

@OptIn(ExperimentalTime::class)
fun main() {
    val durations = problems.associate { problem ->
        val dur = problem.runBoth(timesToRun = 1000)
        Pair(problem.number, dur)
    }
    println("=== Timing summary ===")
    durations.forEach {
        println("Day ${it.key}\t${it.value}")
    }
    println("Total time: ${durations.values.reduce { acc, duration -> acc + duration }}")
}
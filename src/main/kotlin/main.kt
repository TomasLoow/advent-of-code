import kotlin.time.ExperimentalTime

val problems = listOf(
    aoc2022.day1Problem,

)

@OptIn(ExperimentalTime::class)
fun main() {
    val durations = problems.map { problem ->
        val dur = problem.runBoth(timesToRun = 100)
        Pair(problem.number, dur)
    }.toMap()
    println("=== Timing summary ===")
    durations.forEach {
        println("Day ${it.key}\t${it.value}")
    }
    println("Total time: ${durations.values.reduce{acc, duration -> acc + duration }}")
}
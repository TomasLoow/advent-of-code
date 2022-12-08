import utils.ensureNl
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

interface DailyProblem<Res> {
    val number: Int
    val name: String
    val inputFilePath: String
    fun commonParts() {}
    fun part1(): Res
    fun part2(): Res

    fun getInputFile() = File(inputFilePath)

    fun getInputText(): String {
        return File(inputFilePath).readText().ensureNl()
    }


    @ExperimentalTime
    fun runBoth(timesToRun: Int = 1): Duration {
        println("=== Day $number : $name ===")
        var result1: Res? = null
        var result2: Res? = null
        val runDuration = measureTime {
            repeat(timesToRun) {
                this.commonParts()
                result1 = this.part1()
                result2 = this.part2()
            }
        }
        val averageDuration = runDuration / timesToRun
        println("part 1: ${result1.toString()}")
        println("part 2: ${result2.toString()}")
        println("Average runtime for day ${number}: $averageDuration based on $timesToRun runs")
        println("===========")
        println()
        return averageDuration
    }

}
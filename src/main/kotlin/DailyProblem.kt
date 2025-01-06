import aoc.utils.extensionFunctions.ensureNl
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

abstract class DailyProblem<Res> {
    abstract val number: Int
    abstract val year: Int
    abstract val name: String
    var testData = false

    open fun commonParts() {}

    abstract fun part1(): Res
    abstract fun part2(): Res
    fun getInputFile(): File {
        val num = number.toString().padStart(2, '0')
        if (testData) return File("input/aoc$year/testinput/day$num.txt")
        return File("input/aoc$year/day$num.txt")
    }

    fun getInputText(): String {
        return getInputFile().readText().ensureNl()
    }


    @ExperimentalTime
    fun runBoth(timesToRun: Int = 1): Duration {
        println("=== Day $number : $name ===")
        println("https://adventofcode.com/$year/day/$number")
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
        println("Average runtime for year $year day ${number}: $averageDuration based on $timesToRun runs")
        println("===========")
        println()
        return averageDuration
    }

}
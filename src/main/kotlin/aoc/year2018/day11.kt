package aoc.year2018

import aoc.DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Rect
import aoc.utils.geometry.Vector
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

class Day11Problem : DailyProblem<String>() {

    override val number = 11
    override val year = 2018
    override val name = "Chronal Charge"

    private lateinit var grid: Array2D<Int>
    private lateinit var cumSum: Array2D<Int>
    private var inputNumber by Delegates.notNull<Int>()

    override fun commonParts() {
        inputNumber = getInputText().nonEmptyLines().first().toInt()
        grid = Array2D(300, 300, 0)
        grid.yRange.forEach { y ->
            grid.xRange.forEach { x ->
                val rackId = x + 10
                val powerLevel = (rackId * y + inputNumber) * rackId
                grid[x, y] = (powerLevel / 100) % 10 - 5
            }
        }
        /* Set up a cumulative sum array. It's defined by:
        cumSum[x,y] = ∑ grid[i,j] for i < x & j < y

        We can use this in part2 to calculate sums of squares cheaper since the sum of a square with
        top-left corner x,y and size s is:
        cumSum[x+size,y+size] - cumSum[x+size,y] - cumSum[x,y+size] + cumSum[x,y]
         */
        cumSum = Array2D(grid.width + 1, grid.height + 1, 0)
        grid.xRange.forEach { x ->
            grid.yRange.forEach { y ->
                cumSum[x + 1, y + 1] = cumSum[x, y + 1] + cumSum[x + 1, y] - cumSum[x, y] + grid[x, y]
            }
        }

    }

    override fun part1(): String {
        val best = grid.mapAndFilterToListByNotNull { c, _ ->
            if (c + Vector(2, 2) !in grid) null
            else {
                val r = Rect(c, c + Vector(2, 2))
                c to grid.sumInRect(r) { it }
            }
        }.maxBy { it.second }
        return "${best.first.x},${best.first.y}"
    }


    /** NOTE:
     * Since the grid values are uniformly distributed over [-5,-4...4] the expected value is -0.5 and the
     * stddev is about 2.87
     *
     * Adding together N values gives a normal distribution norm(-N/2, sqrt(N)*2.87)
     * We are always adding values in squares so let N = k^2
     * norm(-k^2/2, k*2.87)
     * This value is positiive if the result is more than  k/(2*2.87) stdevs away from the expected value.
     * A normal distribution is within three standard devations with probability 99.85%, so we can see that when k > 17
     * the sum of the vast majority of squares will be negative.
     *
     * So there is no need to test for very big squares since it will be  almost impossible for them to have the best
     * value to  be in, since they are extremely unlikely to even be positive.
     * Testing up to 25 should be enough with room to spare and is probably a bit excessive even.
     */

    override fun part2(): String {
        val bestForEachSize = (3..25).asSequence().map { size ->
            val squareSum = (0..<grid.width - size).asSequence().flatMap { x ->
                (0..<grid.height - size).asSequence().map { y ->
                    Coord(
                        x,
                        y
                    ) to (cumSum[x, y] + cumSum[x + size, y + size] - cumSum[x + size, y] - cumSum[x, y + size])
                }
            }.maxBy { it.second }
            size to squareSum
        }
        println(bestForEachSize.toList())
        val best = bestForEachSize.maxBy { it.second!!.second }
        val (size, coordSumPair) = best!!
        return "${coordSumPair!!.first.x},${coordSumPair.first.y},$size"

    }
}

val day11Problem = Day11Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day11Problem.testData = false
    day11Problem.runBoth(100)
}
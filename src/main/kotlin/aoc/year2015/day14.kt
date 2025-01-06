package aoc.year2015

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.increase
import aoc.utils.extensionFunctions.nonEmptyLines
import kotlin.math.min
import kotlin.time.ExperimentalTime

class Day14Problem : DailyProblem<Int>() {

    override val number = 14
    override val year = 2015
    override val name = "Reindeer Olympics"

    companion object {
        const val STOP_TIME = 2503
    }

    private lateinit var reindeers: List<Reindeer>

    private data class Reindeer(val name: String, val speed: Int, val flyTime: Int, val restTime: Int) {
        fun distanceAfter(time: Int): Int {
            val cycleTime = flyTime + restTime
            val completedCycles = time / cycleTime
            val remainingTime = time % cycleTime
            val lastFlightTime = min(remainingTime, flyTime)
            return (completedCycles*flyTime*speed) + lastFlightTime*speed
        }
    }

    override fun commonParts() {
        val re = """(.*) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds.""".toRegex()
        reindeers = getInputText().nonEmptyLines().map { line ->
            val (name, speed, time, rest) = re.matchEntire(line)!!.destructured
            Reindeer(name, speed.toInt(), time.toInt(), rest.toInt())
        }
    }

    override fun part1(): Int {
        return reindeers.maxOf { r ->
            r.distanceAfter(STOP_TIME)
        }
    }

    override fun part2(): Int {
        val scores = emptyMutableMap<String, Int>()
        reindeers.forEach { scores[it.name] = 0 }
        (1 .. STOP_TIME).forEach { t ->
            val ranking = reindeers.map { r -> Pair(r, r.distanceAfter(t)) }.sortedByDescending { it.second }
            val leaderPos = ranking.first().second
            ranking.filter { (_, p) -> p == leaderPos }.forEach { (r, _) ->
                scores.increase(r.name, 1)
            }
        }
        return scores.values.maxOf {it}
    }
}

val day14Problem = Day14Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day14Problem.runBoth(100)
}
package aoc.year2015

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime
import kotlin.math.*

class Day14Problem() : DailyProblem<Int>() {

    override val number = 14
    override val year = 2015
    override val name = "Reindeer Olympics"

    companion object {
        val stopTime = 2503
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
            r.distanceAfter(stopTime)
        }
    }

    override fun part2(): Int {
        val scores = mutableMapOf<String, Int>()
        reindeers.forEach { scores[it.name] = 0 }
        (1 .. stopTime).forEach { t ->
            val ranking = reindeers.map { r -> Pair(r, r.distanceAfter(t)) }.sortedByDescending { it.second }
            val leaderPos = ranking.first().second
            ranking.filter { (r, p) -> p == leaderPos }.forEach { (r, p) ->
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
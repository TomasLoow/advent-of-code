package aoc.year2022

import aoc.DailyProblem
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.emptyMutableSet
import aoc.utils.extensionFunctions.nonEmptyLines
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.time.ExperimentalTime

private typealias Pit = ArrayDeque<BitSet>
class Day17Problem : DailyProblem<BigInteger>() {

    override val number = 17
    override val year = 2022
    override val name = "Pyroclastic Flow"


    private lateinit var airFlowList: List<Direction>
    private lateinit var tetrisPit: Pit

    class Rock(private var shape: Set<Coord>, var x: Int, var y:Int, private val world: Pit) {

        private val offsetCoords: Collection<Coord>
            get() {
                return shape.map { it.copy(x = it.x+x, y = it.y+y) }
            }

        fun tryToMoveHorizontally(direction: Direction) {
            val dx = when (direction) {
                Direction.RIGHT -> 1
                Direction.LEFT -> -1
                else -> throw Exception("wat?")
            }
            val shifted = offsetCoords.map { coord: Coord -> coord.copy(x=coord.x+dx) }
            if (shifted.all { c -> c.x in (0..6) && !spaceBlocked(c.x, c.y) })
                x += dx
        }

        fun canFallDown(): Boolean {
            return offsetCoords.map{ it + Pair(0,-1)}.all { !spaceBlocked(it.x, it.y) }
        }

        private fun spaceBlocked(x: Int, y:Int): Boolean {
            if (y >= world.size) return false
            return world[y][x]
        }

        fun fallDown() {
            y--
        }

        fun place() {
            val maxY = offsetCoords.maxOf { it.y }
            if(maxY >= world.size - 1) {
                val d = maxY - world.size + 1
                repeat(d) {
                    world.addLast(BitSet(7))
               }
                y - d
            }
            offsetCoords.forEach{ (x,y) -> world[y][x] = true}
        }
    }

    override fun commonParts() {
        airFlowList = getInputText().nonEmptyLines().single().map { c ->
            when (c) {
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                else -> {
                    throw Exception("Bad input")
                }
            }
        }
    }

    private fun makePit() {
        tetrisPit = ArrayDeque()
        val floor = BitSet(7)
        (0..6).forEach { floor[it] = true }
        tetrisPit.addLast(floor)
    }

    private var airIdx = 0
    private var shapeIdx = 0

    private fun getNextFlow(): Direction {
        val res = if (airIdx < airFlowList.size) {
            airFlowList[airIdx]
        } else {
            airIdx = 0
            airFlowList[airIdx]
        }
        airIdx++
        return res
    }
    private fun getNextShape(): Set<Coord> {
        val res = if (shapeIdx < shapes.size) {
            shapes[shapeIdx]
        } else {
            shapeIdx = 0
            shapes[shapeIdx]
        }
        shapeIdx++
        return res
    }

    private fun resetProblem() {
        airIdx = 0
        shapeIdx = 0
        makePit()
    }

    @Suppress("unused")
    private fun printPit() {
        tetrisPit.reversed().forEach { line ->
            (0..6).forEach { print(if (line[it]) "#" else ".") }
            println()
        }
        println()
    }

    private fun spawnRock(tetris: Pit, shape: Set<Coord>): Rock {
        val y = tetris.size + 3
        return Rock(shape, 2, y, tetris)
    }



    fun solve(n: Int): BigInteger {
        val start = tetrisPit.size
        var h = 0.toBigInteger()
        repeat(n) {
            val shape = getNextShape()
            val rock = spawnRock(tetrisPit, shape)

            rock.tryToMoveHorizontally(getNextFlow())
            while (rock.canFallDown()) {
                rock.fallDown()
                val dir = getNextFlow()
                rock.tryToMoveHorizontally(dir)
            }
            rock.place()
            h += cropPit().toBigInteger()
        }
        return h + (tetrisPit.size - start).toBigInteger()
    }

    private fun cropPit(): Int {
        var i = 0

        for ( line in tetrisPit.reversed()) {
            i++
            if ((0..6).all { line[it]}) break
        }
        val numToCrop = tetrisPit.size - i
        repeat(numToCrop) { tetrisPit.removeFirst() }
        return numToCrop
    }

    private fun findCycleLength(): Pair<Int,Int> {
        tetrisPit.hashCode()
        val s = emptyMutableSet<Pair<Int,Int>>()

        // Find an airflow value that has been seen at the start of a new block cycle twice
        var candidate: Int? = null
        for(it in (1..4000)) {
            solve(5)
            val signifier = Pair(airIdx, tetrisPit.takeLast(50).hashCode())
            if (signifier in s) {
                candidate = airIdx
                break
            }
            s.add(signifier)
        }

        /* Find three values for number of steps after which the candidate value is seen.
         * Return the first one (length until first cycle start) and the difference between
         * the second and third (length of all other cycles)
        */
        resetProblem()
        val pattern = mutableListOf<Int>()
        for(it in (1..4000)) {
            solve(5)
            if (airIdx == candidate) {
                pattern.add(it)
                if (pattern.size == 3 && pattern[2] - pattern[1] == pattern[1] - pattern[0])
                    return Pair(5* pattern[0], 5*(pattern[1] - pattern[0]))
            }
        }
        throw Exception("No cycle found")
    }

    override fun part1(): BigInteger {
        resetProblem()
        return solve(2022)
    }

    override fun part2(): BigInteger {
        resetProblem()
        val (firstOffset, repeatNum) = findCycleLength()
        val totalFullLoops = (1000000000000L - firstOffset)/repeatNum
        val lengthOfLastLoop = (1000000000000L - firstOffset) % repeatNum

        resetProblem()
        val h1 = solve(firstOffset)
        resetProblem()
        val h2 = solve(firstOffset + repeatNum)
        resetProblem()
        val h3 = solve(firstOffset + repeatNum*2)
        resetProblem()
        val hlast = solve(firstOffset + repeatNum*2 + lengthOfLastLoop.toInt())

        val heightOfCycle = h3-h2
        val heightOfEndStuff = hlast - h3

        return h1 + totalFullLoops.toBigInteger()*heightOfCycle + heightOfEndStuff

    }

    companion object {
        val shapes = listOf(
            // ####
            setOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(3, 0)),

            // .#.
            // ###
            // .#.
            setOf(Coord(1, 0), Coord(0, 1), Coord(1, 1), Coord(2, 1), Coord(1, 2)), // ####

            // ..#
            // ..#
            // ###
            setOf(Coord(0, 0), Coord(1, 0), Coord(2, 0), Coord(2, 1), Coord(2, 2)), // ####

            // #
            // #
            // #
            // #
            setOf(Coord(0, 0), Coord(0, 1), Coord(0, 2), Coord(0, 3)),

            //##
            //##
            setOf(Coord(0, 0), Coord(1, 1), Coord(0, 1), Coord(1, 0)),

            )
    }
}

val day17Problem = Day17Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day17Problem.testData = false
    day17Problem.runBoth(100)
}
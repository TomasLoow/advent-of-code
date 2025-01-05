package aoc.year2022

import DailyProblem
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.*
import aoc.utils.parseAllPositiveInts
import aoc.utils.parseTwoBlocks
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

sealed interface Move {
    data object L : Move
    data object R : Move
    data class Step(val distance: Int) : Move
}

data class Edge(
    val corner1: Coord,
    val corner2: Coord,
    val dirTowardsInternal: Direction,
    val map: Array2D<Boolean>,
    val faceIdx: Int
) {
    val coords: List<Coord> by lazy {
        val d = if (corner1.x < corner2.x) Direction.RIGHT
        else if (corner1.x > corner2.x) Direction.LEFT
        else if (corner1.y < corner2.y) Direction.DOWN
        else Direction.UP
        buildList<Coord> {
            var c = corner1
            while (c != corner2) {
                add(c)
                c += d
            }
            add(corner2)
        }
    }
}

data class Face(val a: Array2D<Boolean>, val edges: Map<Direction, Edge>)

data class Cube(val faces: Map<Int, Face>, val edgeConnections: Map<Edge, Edge>)

data class Walker(var pos: Coord, var dir: Direction, var activeSide: Int, val cube: Cube) {

    val currentMap: Array2D<Boolean>
        get() = cube.faces[activeSide]!!.a

    fun walk(moves: List<Move>) {
        moves.forEach {
            walk(it)
        }
    }

    private fun walk(move: Move) {
        when (move) {
            Move.R -> dir = dir.rotateCW()
            Move.L -> dir = dir.rotateCCW()
            is Move.Step -> {
                try {
                    repeat(move.distance) { step() }
                } catch (e: Done) {
                    //println("Done $move. On face $activeSide: $pos $dir, BONK")
                    //currentMap.mapIndexed { c, b -> if (c == pos) "@" else if (b) "#" else "."}.print { it }
                    //readLine()
                    return
                }
            }
        }
        //println("Done $move. On face $activeSide: $pos $dir")
        //currentMap.mapIndexed { c, b -> if (c == pos) "@" else if (b) "#" else "."}.print { it }
        //readLine()

    }

    private class Done : Throwable()

    private fun step() {
        val posInFront = pos + dir
        if (posInFront in currentMap) {
            if (currentMap[posInFront]) throw Done()
            else {
                pos = posInFront; return
            }
        } else { // lookAhead not in current face
            val leavingEdge = cube.faces[this.activeSide]!!.edges[dir]!!
            val arrivingEdge = cube.edgeConnections[leavingEdge]!!

            val i = leavingEdge.coords.indexOf(pos)
            val newPos = arrivingEdge.coords[arrivingEdge.coords.size - 1 - i]

            if (arrivingEdge.map[newPos]) throw Done()
            pos = newPos
            dir = arrivingEdge.dirTowardsInternal
            activeSide = arrivingEdge.faceIdx
        }
    }
}

class Day22Problem : DailyProblem<Int>() {

    override val number = 22
    override val year = 2022
    override val name = "Monkey Map"

    private lateinit var facePositions: Map<Int, Coord>
    private lateinit var faces: Map<Int, Face>
    private lateinit var moves: List<Move>

    fun parseSteps(s: String): List<Move> {
        val nums = parseAllPositiveInts(s).map { Move.Step(it) }
        val turns = s.filter { it.isLetter() }.map { c ->
            when (c) {
                'L' -> Move.L
                'R' -> Move.R
                else -> error("Unknown move $c")
            }
        }
        assert(nums.size == turns.size + 1)
        return nums.take(1) + turns.zip(nums.drop(1)) { t, n -> listOf(t, n) }.flatten()
    }

    override fun commonParts() {

        val (fullMap, moveStr) = parseTwoBlocks(
            getInputText(),
            {
                val paddedLines = getInputText().nonEmptyLines().map { it.padEnd(150, ' ').toList().map { it == '#' } }
                Array2D(paddedLines)
            },
            ::parseSteps
        )
        this.moves = moveStr

        var faceDiagonalVector: Vector by Delegates.notNull()
        if (!testData) {
            this.facePositions = mapOf(
                1 to Coord(100, 0),
                2 to Coord(50, 0),
                3 to Coord(50, 50),
                4 to Coord(50, 100),
                5 to Coord(0, 100),
                6 to Coord(0, 150),
            )
            faceDiagonalVector = Vector(49, 49)
        } else {
            this.facePositions = mapOf(
                1 to Coord(8, 0),
                2 to Coord(0, 4),
                3 to Coord(4, 4),
                4 to Coord(8, 4),
                5 to Coord(8, 8),
                6 to Coord(12, 8),
            )
            faceDiagonalVector = Vector(3, 3)

        }

        val map1 = fullMap[Rect(facePositions[1]!!, facePositions[1]!! + faceDiagonalVector)]
        val map2 = fullMap[Rect(facePositions[2]!!, facePositions[2]!! + faceDiagonalVector)]
        val map3 = fullMap[Rect(facePositions[3]!!, facePositions[3]!! + faceDiagonalVector)]
        val map4 = fullMap[Rect(facePositions[4]!!, facePositions[4]!! + faceDiagonalVector)]
        val map5 = fullMap[Rect(facePositions[5]!!, facePositions[5]!! + faceDiagonalVector)]
        val map6 = fullMap[Rect(facePositions[6]!!, facePositions[6]!! + faceDiagonalVector)]


        faces = mapOf(
            1 to Face(map1, edgesOf(map1, 1)),
            2 to Face(map2, edgesOf(map2, 2)),
            3 to Face(map3, edgesOf(map3, 3)),
            4 to Face(map4, edgesOf(map4, 4)),
            5 to Face(map5, edgesOf(map5, 5)),
            6 to Face(map6, edgesOf(map6, 6)),
        )
    }

    fun edgesOf(map: Array2D<Boolean>, idx: Int): Map<Direction, Edge> {
        return mapOf(
            Direction.UP to Edge(map.rect.topLeft, map.rect.topRight, Direction.DOWN, map, idx),
            Direction.RIGHT to Edge(map.rect.topRight, map.rect.bottomRight, Direction.LEFT, map, idx),
            Direction.DOWN to Edge(map.rect.bottomRight, map.rect.bottomLeft, Direction.UP, map, idx),
            Direction.LEFT to Edge(map.rect.bottomLeft, map.rect.topLeft, Direction.RIGHT, map, idx),
        )
    }


    override fun part1(): Int {
        val cube = if (testData) makeTestCubePt1(faces) else makeCubePt1(faces)
        val startSide = if (testData) 1 else 2
        val w = Walker(Coord(0, 0), Direction.RIGHT, startSide, cube)
        w.walk(moves)
        val faceOffestInOriginalMap = facePositions[w.activeSide]!!
        val p = w.pos + faceOffestInOriginalMap.toVector()

        val dirValue = listOf(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP).indexOf(w.dir)
        return (p.y + 1) * 1000 + (p.x + 1) * 4 + dirValue
    }

    override fun part2(): Int {
        val cube = if (testData) makeTestCubePt2(faces) else makeCubePt2(faces)
        val startSide = if (testData) 1 else 2
        val w = Walker(Coord(0, 0), Direction.RIGHT, startSide, cube)
        w.walk(moves)
        val faceOffestInOriginalMap = facePositions[w.activeSide]!!
        val p = w.pos + faceOffestInOriginalMap.toVector()

        val dirValue = listOf(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP).indexOf(w.dir)
        return (p.y + 1) * 1000 + (p.x + 1) * 4 + dirValue
    }
}

val day22Problem = Day22Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day22Problem.testData = true
    day22Problem.runBoth(10)
}




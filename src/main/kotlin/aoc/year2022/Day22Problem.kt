package aoc.year2022

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

sealed interface Move {
    class Rotate(val dir: Direction) : Move
    class Forward(val steps: Int) : Move
}


class Day22Problem : DailyProblem<Long>() {
    override val number = 22
    override val year = 2022

    override val name = "Monkey Map"
    private lateinit var map: Array2D<Char>

    private lateinit var moves: List<Move>

    override fun commonParts() {
        fun parseMoves(s: String): List<Move> {
            var i = 0
            var curS = s.trim().substring(i)
            return buildList {
                while (true) {
                    val steps = curS.takeWhile { it.isDigit() }
                    this.add(Move.Forward(steps.toInt()))
                    i += steps.length
                    curS = s.trim().substring(i)
                    if (curS.isEmpty()) break
                    val d = curS.first()
                    this.add(Move.Rotate(if (d == 'R') Direction.RIGHT else Direction.LEFT))
                    i += 1
                    curS = s.trim().substring(i)
                }
            }
        }

        fun parseMap(s: String): Array2D<Char> {
            val mx = s.nonEmptyLines().maxOf { it.length }
            val corrected = s.nonEmptyLines().joinToString("\n") { line ->
                if (line.length < mx) {
                    line + " ".repeat(mx - line.length)
                } else line
            }
            return Array2D.parseFromLines(corrected, ::id)
        }

        val parsed = parseTwoBlocks(getInputText(), ::parseMap, ::parseMoves)
        map = parsed.first
        moves = parsed.second
        // map.print { it.toString() }
    }


    fun findStartPos(): Coord {
        return Coord(map.xRange.first { map[it, 0] == '.' }, 0)
    }


    private fun Array2D.Cursor<Char>.moveUntilWallSkippingEmpty(dir: Direction, distance: Int): Boolean {
        var distLeft = distance
        var lastGroundPos = this.coord
        while (distLeft > 0) {
            this.move(dir, wrapping = true)
            when (this.value) {
                '#' -> {
                    this.moveTo(lastGroundPos)
                    return true
                }

                '.' -> {
                    distLeft--
                    lastGroundPos = this.coord
                }

                ' ' -> {/* nop */
                }
            }
        }
        return false
    }

    override fun part1(): Long {
        var dir = Direction.RIGHT
        val curs = map.cursor(findStartPos())
        // printMap(curs.coord)
        for ((loopIndex, move) in moves.withIndex()) {
            when (move) {
                is Move.Forward -> curs.moveUntilWallSkippingEmpty(dir, move.steps)
                is Move.Rotate -> dir = if (move.dir == Direction.RIGHT) dir.rotateCW() else dir.rotateCCW()
            }
            // printMap(curs.coord)
            saveMapImage(curs.coord, loopIndex)
            // println()
        }
        return (1000 * (curs.coord.y + 1) + 4 * (curs.coord.x + 1) + dirScore(dir)).toLong()
    }

    private fun saveMapImage(coord: Coord, idx: Int) {
        map.mapIndexed { pos, c -> if (pos == coord) "X" else c.toString() }.toImage(8, "walk$idx.pbm") { s ->
            when (s) {
                " " -> 7
                "." -> 6
                "X" -> 3
                "#" -> 0
                else -> throw Exception()
            }
        }
    }

    private fun printMap(coord: Coord) {
        map.mapIndexed { pos, c -> if (pos == coord) "X" else c.toString() }.print { it }
    }


    private fun dirScore(dir: Direction): Int {
        return when (dir) {
            // Facing is 0 for right (>), 1 for down (v), 2 for left (<), and 3 for up (^).
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
            Direction.LEFT -> 2
            Direction.UP -> 3
            else -> throw Exception("Bad dir")
        }
    }
    override fun part2(): Long {
        return 1
    }
}

typealias FaceIndex = Int
enum class Rotation {
    NONE, CW, HALFTURN, CCW
}
class Cube(val faces: List<Array2D<Char>> = TODO()) {
    val connections: MutableMap<Pair<FaceIndex, Direction>, Pair<FaceIndex, Rotation>> = emptyMutableMap()
}

val day22Problem = Day22Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    val s = """
        ...
        ...
        ...""".trimIndent()

    /*
            000111
            000111
            000111
            222
            222
            222
        444333
        444333
        444333
        555
        555
        555
     */

    val face0 = Array2D.parseFromLines(s) { it }
    val face1 = Array2D.parseFromLines(s) { it }
    val face2 = Array2D.parseFromLines(s) { it }
    val cube = Cube(faces = listOf(face0, face1,face2))
    cube.connections[Pair(0, Direction.RIGHT)] = Pair(1, Rotation.NONE)
    cube.connections[Pair(0, Direction.DOWN)] = Pair(2, Rotation.NONE)

    cube.connections[Pair(1, Direction.LEFT)] = Pair(0, Rotation.NONE)
    cube.connections[Pair(1, Direction.DOWN)] = Pair(2, Rotation.CW)
    cube.connections[Pair(1, Direction.RIGHT)] = Pair(3, Rotation.HALFTURN)

    cube.connections[Pair(2, Direction.UP)] = Pair(0, Rotation.NONE)
    cube.connections[Pair(2, Direction.RIGHT)] = Pair(1, Rotation.CCW)
    cube.connections[Pair(2, Direction.DOWN)] = Pair(3, Rotation.NONE)

    cube.connections[Pair(3, Direction.UP)] = Pair(2, Rotation.NONE)
    cube.connections[Pair(3, Direction.RIGHT)] = Pair(1, Rotation.HALFTURN)



    day22Problem.testData = false
    day22Problem.runBoth(100)
}
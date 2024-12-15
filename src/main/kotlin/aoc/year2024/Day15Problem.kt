package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

class Day15Problem : DailyProblem<Int>() {

    override val number = 15
    override val year = 2024
    override val name = "Warehouse Woes"

    private lateinit var initialMap: Array2D<Char>
    private lateinit var moves: List<Direction>
    private lateinit var playerStartPos: Coord

    override fun commonParts() {
        fun parseDirs(s: String): List<Direction> {
            return s.filter { it in "><^v" }.map(::parseDirectionFromArrow)
        }

        val (mp, mv) = parseTwoBlocks(getInputText(), ::parseCharArray, ::parseDirs)
        initialMap = mp

        moves = mv
        playerStartPos = initialMap.findIndexedByCoordinate { c, v -> v == '@' }!!.first
        initialMap[playerStartPos] = '.'
    }

    override fun part1(): Int {
        val map = initialMap.map(::id)

        val cursor = map.cursor(playerStartPos, false)

        for (move in moves) {
            cursor.move(move)
            if (cursor.value == '#') cursor.back()
            if (cursor.value == 'O') {
                val consecutive = cursor.coord.walkInDir(move).takeWhile { map[it] == 'O' }.toList()
                val afterConsecutive = consecutive.last() + move
                if (map[afterConsecutive] == '#') {
                    cursor.back()
                } else {
                    map[consecutive.first()] = '.'
                    map[afterConsecutive] = 'O'
                }
            }
            // showPart1(map, cursor)
        }
        return map.mapAndFilterToListByNotNull { coord, c ->
            if (c == 'O') {
                100 * coord.y + coord.x
            } else null
        }.sum()
    }

    private fun showPart1(map: Array2D<Char>, cursor: Array2D.Cursor<Char>) {
        map.mapIndexed() { c, v -> if (c == cursor.coord) '@' else v }.print { it.toString() }
    }

    private data class Box(var coordOfLeft: Coord) {
        val coordOfRight: Coord
            get() {
                return coordOfLeft + Vector(1, 0)
            }

        operator fun contains(coord: Coord): Boolean = (coord == coordOfLeft || coord == coordOfRight)
    }

    private fun canPushBox(map: Array2D<Char>, boxes: List<Box>, box: Box, dir: Direction): Boolean {
        when (dir) {
            Direction.LEFT -> {
                val movingTo = box.coordOfLeft + dir
                if (map[movingTo] == '#') {
                    return false
                }
                val collidesWith = boxes.find { it.coordOfRight == movingTo }
                return collidesWith == null || canPushBox(map, boxes, collidesWith, dir)
            }

            Direction.RIGHT -> {
                val movingTo = box.coordOfRight + dir
                if (map[movingTo] == '#') {
                    return false
                }
                val collidesWith = boxes.find { it.coordOfLeft == movingTo }
                return collidesWith == null || canPushBox(map, boxes, collidesWith, dir)
            }

            Direction.UP, Direction.DOWN -> {
                val (movingTo1, movingTo2) = (box.coordOfLeft + dir to box.coordOfRight + dir)
                if (map[movingTo1] == '#' || map[movingTo2] == '#') {
                    return false
                }
                val collidesWith = boxes.filter { movingTo1 in it || movingTo2 in it }
                return collidesWith.isEmpty() || collidesWith.all { canPushBox(map, boxes, it, dir) }
            }

            else -> throw Exception("Invalid dir for problem")
        }
    }

    private fun doPushBox(map: Array2D<Char>, boxes: List<Box>, box: Box, dir: Direction) {
        when (dir) {
            Direction.LEFT -> {
                val movingTo = box.coordOfLeft + dir
                val collidesWith = boxes.find { it.coordOfRight == movingTo }
                box.coordOfLeft = box.coordOfLeft + dir
                if (collidesWith != null) doPushBox(map, boxes, collidesWith, dir)
            }

            Direction.RIGHT -> {
                val movingTo = box.coordOfRight + dir
                val collidesWith = boxes.find { it.coordOfLeft == movingTo }
                box.coordOfLeft = box.coordOfLeft + dir
                if (collidesWith != null) doPushBox(map, boxes, collidesWith, dir)
            }

            Direction.UP, Direction.DOWN -> {
                val (movingTo1, movingTo2) = (box.coordOfLeft + dir to box.coordOfRight + dir)
                val collidesWith = boxes.filter { movingTo1 in it || movingTo2 in it }
                box.coordOfLeft = box.coordOfLeft + dir
                collidesWith.forEach { doPushBox(map, boxes, it, dir) }
            }

            else -> throw Exception("Invalid dir for problem")
        }

    }


    override fun part2(): Int {
        val map = Array2D<Char>(
            initialMap.width * 2,
            initialMap.height
        ) { (x, y) -> if (initialMap[x / 2, y] == '#') '#' else '.' }
        val startPos = playerStartPos.copy(x = playerStartPos.x * 2)
        val boxes = initialMap.mapAndFilterToListByNotNull { coord, c ->
            if (c == 'O') Box(coord.copy(x = coord.x * 2)) else null
        }

        val cursor = map.cursor(startPos, false)

        for (move in moves) {
            //showPart2(map, cursor, boxes)

            cursor.move(move)
            if (cursor.value == '#') {
                cursor.back()
                continue
            }

            val boxAt = kotlin.runCatching {
                boxes.first { box -> box.coordOfLeft == cursor.coord || box.coordOfRight == cursor.coord }
            }
            if (boxAt.isFailure) continue
            val box = boxAt.getOrThrow()
            val pushSucceed = canPushBox(map, boxes, box, move)
            if (!pushSucceed) cursor.back()
            else doPushBox(map, boxes, box, move)

        }

        //showPart2(map, cursor, boxes)

        return boxes.sumOf { box -> box.coordOfLeft.y * 100 + box.coordOfLeft.x }
    }

    private fun showPart2(
        map: Array2D<Char>,
        cursor: Array2D.Cursor<Char>,
        boxes: List<Box>
    ) {
        map.mapIndexed() { c, v ->
            if (c == cursor.coord) '@'
            else if (boxes.any { box -> box.coordOfLeft == c }) '['
            else if (boxes.any { box -> box.coordOfRight == c }) ']'
            else v
        }.print { it.toString() }
    }
}

val day15Problem = Day15Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day15Problem.testData = false
    day15Problem.runBoth(100)
}
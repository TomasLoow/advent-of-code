package aoc.utils

import aoc.utils.Direction.*
import java.io.BufferedWriter
import java.io.FileWriter
import kotlin.math.absoluteValue

@Suppress("UNCHECKED_CAST")
class Array2D<T> {

    val height: Int
    val width: Int
    val rect: Rect

    private val data: Array<Any?>

    constructor(input: Collection<Collection<T>>) {
        this.height = input.size
        this.width = input.first().size
        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))

        val flatInput = input.flatten()
        this.data = flatInput.toTypedArray()
    }

    constructor(raw: Collection<T>, width: Int, height: Int) {
        assert(raw.size == height * width)
        this.width = width
        this.height = height
        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))
        this.data = raw.toTypedArray()
    }

    constructor(width: Int, height: Int, initital: T) {
        this.width = width
        this.height = height
        val capacity = width * height

        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))


        this.data = Array<Any?>(capacity) { initital as Any }
    }

    constructor(width: Int, height: Int, f: (Coord) -> T) {
        this.width = width
        this.height = height
        val capacity = width * height

        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))
        this.data = Array(capacity) { i -> f(idx2c(i)) as Any }
    }


    val xRange: IntRange
        get() {
            return rect.xRange
        }

    val yRange: IntRange
        get() {
            return rect.yRange
        }

    val allCoords: List<Coord>
        get() {
            return buildList {
                yRange.forEach { y ->
                    xRange.forEach { x ->
                        add(Coord(x, y))
                    }
                }
            }
        }

    private fun c2Idx(x: Int, y: Int): Int {
        return y * width + x
    }

    private fun c2Idx(c: Coord): Int {
        return c.y * width + c.x
    }

    private fun idx2c(idx: Int): Coord {
        return Coord(idx % width, idx / width)
    }

    operator fun get(x: Int, y: Int): T {

        return data[c2Idx(x, y)] as T
    }

    operator fun get(c: Coord): T {

        return data[c2Idx(c)] as T
    }

    /** Extract a sub array */
    operator fun get(r: Rect): Array2D<T> {
        val newHeight = r.height
        val newWidth = r.width
        val raw = buildList {
            r.yRange.map { y ->
                r.xRange.map { x ->
                    add(this@Array2D[x, y])
                }
            }
        }
        return Array2D(raw, newWidth, newHeight)
    }

    operator fun set(x: Int, y: Int, value: T) {
        data[c2Idx(x, y)] = value as Any
    }

    operator fun set(c: Coord, value: T) {
        data[c2Idx(c)] = value as Any
    }

    /** Sets all elements in a rectangular grid to the same value */
    operator fun set(r: Rect, value: T) {
        r.yRange.forEach { y ->
            val startIdx = c2Idx(r.xRange.first, y)
            val lastIdx = c2Idx(r.xRange.last, y)
            (startIdx..lastIdx).forEach { idx: Int ->
                data[idx] = value as Any
            }
        }
    }

    fun modifyArea(r: Rect, mod: (T) -> T) {
        r.yRange.forEach { y ->
            val startIdx = c2Idx(r.xRange.first, y)
            val lastIdx = c2Idx(r.xRange.last, y)
            (startIdx..lastIdx).forEach { idx: Int ->

                data[idx] = mod(data[idx] as T) as Any
            }
        }
    }

    operator fun contains(c: Coord): Boolean {
        return c.x >= 0 && c.y >= 0 && c.x < width && c.y < height
    }

    fun onEdge(c: Coord): Boolean {
        return (c.x == 0 || c.y == 0 || c.x == this.width - 1 || c.y == this.height - 1)
    }


    fun onCorner(c: Coord): Boolean {
        return (c.x == 0 || c.x == this.width - 1) && (c.y == 0 || c.y == this.height - 1)

    }

    fun neighbourCoords(c: Coord, diagonal: Boolean = true): List<Coord> {
        val pattern = if (diagonal) IDX_STEPS_WITH_DIAG else STEPS_WITHOUT_DIAG
        val idx = c2Idx(c)

        return pattern.map { it + idx }.filter {
            it >= 0 && it < data.size && ((it % width) - (c.x)).absoluteValue <= 1
        }.map(::idx2c)
    }

    fun neighbourValues(c: Coord, diagonal: Boolean = true): List<T> {
        val pattern = if (diagonal) IDX_STEPS_WITH_DIAG else STEPS_WITHOUT_DIAG
        val idx = c2Idx(c)


        return pattern.map { it + idx }.filter { nIdx ->
            nIdx >= 0 && nIdx < data.size && ((nIdx % width) - (c.x)).absoluteValue <= 1
        }.map { nIdx -> data[nIdx] as T }
    }

    fun neighbourCoordsAndValues(c: Coord, diagonal: Boolean = true): Map<Coord, T> {
        return neighbourCoords(c, diagonal).associateWith { coordinate -> get(coordinate) }
    }

    fun <R> map(function: (T) -> R): Array2D<R> {

        return Array2D(data.map(function as ((Any?) -> R)), width, height)
    }

    fun <R> mapIndexed(function: (Coord, T) -> R): Array2D<R> {
        return Array2D(allCoords.zip(data).map { (c, v) -> function(c, v as T) }, width, height)
    }


    fun <R> mapListIndexedByCoordinate(function: (Coord, T) -> R): List<R> {
        return buildList {
            repeat(height) { y ->
                repeat(width) { x ->
                    add(function(Coord(x, y), this@Array2D[x, y]))
                }
            }
        }
    }

    fun countIndexedByCoordinate(function: (Coord, T) -> Boolean): Int {
        var counter = 0
        repeat(height) { y ->
            repeat(width) { x ->
                if (function(Coord(x, y), this[x, y])) counter++
            }
        }
        return counter
    }

    fun findIndexedByCoordinate(function: (Coord, T) -> Boolean): Pair<Coord, T>? {
        repeat(height) { y ->
            repeat(width) { x ->
                val coord = Coord(x, y)
                val value = this[x, y]
                if (function(coord, value)) return Pair(coord, value)
            }
        }
        return null
    }

    fun filterIndexedByCoordinate(function: (Coord, T) -> Boolean): List<Pair<Coord, T>> {
        return buildList {
            repeat(height) { y ->
                repeat(width) { x ->
                    val coord = Coord(x, y)
                    val value = this@Array2D[x, y]
                    if (function(coord, value)) {
                        add(Pair(coord, value))
                    }
                }
            }
        }
    }

    fun show(renderer: (T) -> String): String {
        return buildList {
            rect.yRange.forEach { y ->
                rect.xRange.forEach { x ->
                    add(renderer(this@Array2D[x, y]))
                }
                add("\n")
            }
        }.joinToString("")
    }

    fun print(renderer: (T) -> String) {
        print(show(renderer))
    }

    fun cursor(coord: Coord): Cursor<T> {
        return Cursor(this, coord)
    }

    fun clone(): Array2D<T> {
        return Array2D(this.data.toList() as List<T>, this.width, this.height)
    }

    fun shiftDown(lines: Int, fill: T) {
        val shift = width * lines
        (data.size - 1 downTo shift).forEach { idx ->
            data[idx] = data[idx - shift]
        }
        (0 until shift).forEach { data[it] = fill }
    }

    fun shiftUp(lines: Int, fill: T) {
        val shift = width * lines
        (0 until data.size - shift).forEach { idx ->
            data[idx] = data[idx + shift]
        }
        (data.size - shift until data.size).forEach { data[it] = fill }
    }

    fun toImage(numColors: Int, filename: String    , func: (T) -> Int) {
        val bw = BufferedWriter(FileWriter(filename))
        bw.write("P2\n")
        bw.write("${this.width} ${this.height}\n")
        bw.write("$numColors\n")
        bw.write("\n")

        yRange.forEach { y ->
            xRange.forEach { x ->
                bw.write("${func(this[x,y])} ")
            }
            bw.write("\n")
        }
        bw.close()
    }

    private val IDX_STEPS_WITH_DIAG: Array<Int>
        get() = arrayOf(
            width,
            width - 1,
            width + 1,
            1,
            -1,
            -(width),
            -(width - 1),
            -(width + 1),
        )

    private val STEPS_WITHOUT_DIAG: Array<Int>
        get() = arrayOf(
            width,
            1,
            -1,
            -width,
        )

    companion object {

        fun <T> parseFromLines(string: String, charParser: (Char) -> T): Array2D<T> {
            val lines = string.nonEmptyLines()
            return Array2D(lines.map { line ->
                line.map(charParser)
            })
        }

        fun a2renderBool(b: Boolean): String = if (b) "█" else " "
        fun a2renderInt(b: Int): String = b.toString()

    }


    class Cursor<T>(private val map: Array2D<T>, coord: Coord) {
        private var x: Int
        private var y: Int
        private var idx: Int
        private var prevIdx: Int? = null

        init {
            x = coord.x
            y = coord.y
            idx = map.c2Idx(coord)
        }

        val value: T
            get() = map.data[idx] as T

        val coord: Coord
            get() = map.idx2c(idx)

        val prev: Coord
            get() {
                if (prevIdx == null) throw Exception("No previous coordinate")
                return map.idx2c(prevIdx!!)
            }

        fun set(new: T) {
            map.data[idx] = new
        }

        fun moveRight(wrapping: Boolean=false): Boolean {
            if (x == map.width - 1) {
                if (wrapping) {
                    x = 0
                    idx = map.c2Idx(x,y)
                    return true
                } else return false
            }
            x += 1
            prevIdx = idx
            idx += 1
            return true
        }

        fun moveLeft(wrapping: Boolean=false): Boolean {
            if (x == 0) {
                if (wrapping) {
                    x = map.width - 1
                    idx = map.c2Idx(x,y)
                    return true

                } else return false
            }
            x -= 1
            prevIdx = idx
            idx -= 1
            return true
        }

        fun moveDown(wrapping: Boolean=false): Boolean {
            if (y == map.height - 1) {
                if (wrapping) {
                    y = 0
                    idx = map.c2Idx(x,y)
                    return true

                } else return false
            }
            y += 1
            prevIdx = idx
            idx += map.width
            return true
        }

        fun moveUp(wrapping: Boolean=false): Boolean {
            if (y == 0) {
                if (wrapping) {
                    y = map.height - 1
                    idx = map.c2Idx(x,y)
                    return true

                } else return false
            }
            y -= 1
            prevIdx = idx
            idx -= map.width
            return true
        }

        fun moveUpRight(wrapping: Boolean=false): Boolean {
            if (y == 0 || x == map.width - 1) return if (wrapping) TODO() else false
            y -= 1
            x += 1
            prevIdx = idx
            idx -= (map.width - 1)
            return true
        }

        fun moveUpLeft(wrapping: Boolean=false): Boolean {
            if (y == 0 || x == 0) return if (wrapping) TODO() else false
            y -= 1
            x -= 1
            prevIdx = idx
            idx -= (map.width + 1)
            return true
        }

        fun moveDownRight(wrapping: Boolean=false): Boolean {
            if (y == map.height - 1 || x == map.width - 1) return if (wrapping) TODO() else false
            y += 1
            x += 1
            prevIdx = idx
            idx += (map.width + 1)
            return true
        }

        fun moveDownLeft(wrapping: Boolean=false): Boolean {
            if (y == map.height - 1 || x == 0) return if (wrapping) TODO() else false
            y += 1
            x -= 1
            prevIdx = idx
            idx += (map.width - 1)
            return true
        }

        fun move(dir: Direction, wrapping: Boolean): Boolean {
            return when (dir) {
                UP -> moveUp(wrapping)
                RIGHT -> moveRight(wrapping)
                DOWN -> moveDown(wrapping)
                LEFT -> moveLeft(wrapping)
                UPRIGHT -> moveUpRight(wrapping)
                UPLEFT -> moveUpLeft(wrapping)
                DOWNRIGHT -> moveDownRight(wrapping)
                DOWNLEFT -> moveDownLeft(wrapping)
            }
        }

        fun moveTo(pos: Coord) {
            this.x = pos.x
            this.y = pos.y
            this.idx = map.c2Idx(pos)
        }
    }
}

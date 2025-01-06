@file:Suppress("unused")

package aoc.utils.geometry

import aoc.utils.extensionFunctions.nonEmptyLines
import java.io.BufferedWriter
import java.io.FileWriter
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

@Suppress("UNCHECKED_CAST")
class Array2D<T : Any> {

    var height by Delegates.notNull<Int>()
    var width by Delegates.notNull<Int>()
    lateinit var rect: Rect

    internal val data: Array<Any>

    constructor(input: Collection<Collection<T>>) {
        this.height = input.size
        this.width = input.first().size
        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))

        val flatInput = input.flatten()
        this.data = flatInput.toTypedArray()
    }

    constructor(raw: Collection<T>, width: Int, height: Int) {
        check(raw.size == height * width)
        this.width = width
        this.height = height
        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))
        this.data = raw.toTypedArray()
    }

    constructor(width: Int, height: Int, initial: T) {
        this.width = width
        this.height = height
        val capacity = width * height

        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))


        this.data = Array(capacity) { initial }
    }

    constructor(width: Int, height: Int, f: (Coord) -> T) {
        this.width = width
        this.height = height
        val capacity = width * height

        this.rect = Rect(Coord(0, 0), Coord(width - 1, height - 1))
        this.data = Array(capacity) { i -> f(idx2c(i)) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Array2D<*>
        return (height == other.height && width == other.width && data.contentEquals(other.data))
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

    internal fun c2Idx(x: Int, y: Int): Int {
        return y * width + x
    }

    internal fun c2Idx(c: Coord): Int {
        return c.y * width + c.x
    }

    internal fun idx2c(idx: Int): Coord {
        return Coord(idx % width, idx / width)
    }

    operator fun get(x: Int, y: Int): T {

        return data[c2Idx(x, y)] as T
    }

    operator fun get(c: Coord): T {
        return data[c2Idx(c)] as T
    }

    operator fun get(c: Coord, dir: Direction): List<T> {
        return c.walkInDir(dir).takeWhile { this.contains(it) }.map { this[it] }.toList()
    }

    operator fun get(c: Coord, dir: Direction, steps: Int): List<T> {
        return c.walkInDir(dir).takeWhile { this.contains(it) }.take(steps).map { this[it] }.toList()
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
        return c in rect
    }

    fun onEdge(c: Coord): Boolean {
        return (c.x == 0 || c.y == 0 || c.x == this.width - 1 || c.y == this.height - 1)
    }


    @Suppress("unused")
    fun onCorner(c: Coord): Boolean {
        return (c.x == 0 || c.x == this.width - 1) && (c.y == 0 || c.y == this.height - 1)

    }

    fun neighbourCoords(c: Coord, diagonal: Boolean = true): List<Coord> {
        val pattern = if (diagonal) stepsWithDiagonals else stepsWithoutDiagonals
        val idx = c2Idx(c)

        return pattern.map { it + idx }.filter {
            it >= 0 && it < data.size && ((it % width) - (c.x)).absoluteValue <= 1
        }.map(::idx2c)
    }

    fun neighbourValues(c: Coord, diagonal: Boolean = true): List<T> {
        val pattern = if (diagonal) stepsWithDiagonals else stepsWithoutDiagonals
        val idx = c2Idx(c)


        return pattern.map { it + idx }.filter { nIdx ->
            nIdx >= 0 && nIdx < data.size && ((nIdx % width) - (c.x)).absoluteValue <= 1
        }.map { nIdx -> data[nIdx] as T }
    }

    fun neighbourCoordsAndValues(c: Coord, diagonal: Boolean = true): Map<Coord, T> {
        return neighbourCoords(c, diagonal).associateWith { coordinate -> get(coordinate) }
    }

    fun <R : Any> map(function: (T) -> R): Array2D<R> {
        return Array2D(data.map { function(it as T) }, width, height)
    }

    fun <R : Any> mapIndexed(function: (Coord, T) -> R): Array2D<R> {
        return Array2D(allCoords.zip(data).map { (c, v) -> function(c, v as T) }, width, height)
    }

    /** like mapIndexed but saves the data into an already existing array. Nice for performance. */
    fun <R : Any> mapIndexedInto(intoArray: Array2D<R>, function: (Coord, T) -> R) {
        if (rect != intoArray.rect) throw IllegalArgumentException("Arrays must have same dimensions")
        this.forEach { c, v ->
            intoArray.set(c, function(c, v))
        }
    }

    /** like map but saves the data into an already existing array. Nice for performance. */
    fun <R : Any> mapInto(intoArray: Array2D<R>, function: (T) -> R) {
        if (rect != intoArray.rect) throw IllegalArgumentException("Arrays must have same dimensions")
        this.forEach { c, v ->
            intoArray.set(c, function(v))
        }
    }



    fun countIndexedByCoordinate(function: (Coord, T) -> Boolean): Int {
        var counter = 0
        data.forEachIndexed { idx, v ->
            if (function(idx2c(idx), v as T)) counter++
        }
        return counter
    }

    fun count(function: (T) -> Boolean): Int {
        return data.count { function(it as T) }
    }


    fun closest (c: Coord, function: (Coord, T) -> Boolean): Coord? {
        // TODO Optimize!
        return filterToList(function).sortedBy { c.manhattanDistanceTo(it.first) }.firstOrNull()?.first
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

    fun <Q> mapAndFilterToListByNotNull(block: (Coord, T) -> Q?): List<Q> {
        return buildList {
            data.forEachIndexed { index, value ->
                val r = block(idx2c(index), value as T)
                if (r != null) add(r)
            }
        }
    }

    fun filterToList(function: (Coord, T) -> Boolean): List<Pair<Coord, T>> {
        return mapAndFilterToListByNotNull { c, v ->
            if (function(c, v)) Pair(c, v) else null
        }
    }

    fun forEach(block: (Coord, T) -> Any) {
        data.forEachIndexed { index, value ->
            block(idx2c(index), value as T)
        }
    }

    fun floodFill(coord: Coord): Set<Coord> {
        val v = get(coord)
        val filled = mutableSetOf<Coord>()
        val toVisitQueue = mutableListOf(coord)
        while (toVisitQueue.isNotEmpty()) {
            val possibleCoord = toVisitQueue.removeFirst()
            if (possibleCoord in filled) continue
            filled.add(possibleCoord)
            toVisitQueue.addAll(
                neighbourCoords(possibleCoord, diagonal = false).filter { get(it) == v && it !in filled })
        }
        return filled
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

    fun cursor(coord: Coord, wrapping: Boolean = false): Cursor<T> {
        return if (wrapping) WrappingCursor(this, coord)
        else Cursor(this, coord)
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

    @Suppress("unused")
    fun toImage(numColors: Int, filename: String, func: (T) -> Int) {
        val bw = BufferedWriter(FileWriter(filename))
        bw.write("P2\n")
        bw.write("${this.width} ${this.height}\n")
        bw.write("$numColors\n")
        bw.write("\n")

        yRange.forEach { y ->
            xRange.forEach { x ->
                bw.write("${func(this[x, y])} ")
            }
            bw.write("\n")
        }
        bw.close()
    }

    /**
     * Returns the coordinates within a specified Manhattan distance from a given coordinate.
     *
     * @param c Center coordinate.
     * @param d Maximum Manhattan distance.
     * @return List of coordinates within the distance.
     */
    fun coordsWithin(c: Coord, d: Int): List<Coord> {
        return buildList {
            for (dx in -d..d) {
                val remainingDist = d - kotlin.math.abs(dx)
                for (dy in -remainingDist..remainingDist) {
                    val new = Coord(c.x + dx, c.y + dy)
                    if (new in this@Array2D) {
                        add(new)
                    }
                }
            }
        }
    }

    fun sumInRect(r: Rect, function: (T) -> Int): Int {
        return r.allCoords.sumOf { c -> function(this[c]) }
    }

    private val stepsWithDiagonals: Array<Int> by lazy {
        arrayOf(
            width,
            width - 1,
            width + 1,
            1,
            -1,
            -(width),
            -(width - 1),
            -(width + 1),
        )
    }

    private val stepsWithoutDiagonals: Array<Int> by lazy {
        arrayOf(
            width,
            1,
            -1,
            -width,
        )
    }

    companion object {

        fun <T : Any> parseFromLines(string: String, charParser: (Char) -> T): Array2D<T> {
            val lines = string.nonEmptyLines()
            return Array2D(lines.map { line ->
                line.map(charParser)
            })
        }

        fun renderBool(a: Boolean): String = if (a) "█" else " "
        fun renderInt(a: Int): String = a.toString()
        fun renderChar(a: Char): String = a.toString()
    }
}

fun showCharArray(a: Array2D<Char>) = a.show { it.toString() }
fun showIntArray(a: Array2D<Int>) = a.show { it.toString() }
fun showBooleanArray(a: Array2D<Boolean>) = a.show { if (it) "█" else " " }

fun printCharArray(a: Array2D<Char>) = println(showCharArray(a))
fun printIntArray(a: Array2D<Int>) = println(showIntArray(a))
fun printBooleanArray(a: Array2D<Boolean>) = println(showBooleanArray(a))

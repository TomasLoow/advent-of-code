package aoc.utils

class Array2D<T> {

    val height: Int
    val width: Int
    private val data: ArrayList<T>
    constructor(input: Collection<Collection<T>>) {
        this.height = input.size
        this.width = input.first().size
        this.data = ArrayList(input.flatten())
    }
    constructor(raw: Collection<T>, width: Int, height:Int) {
        assert(raw.size == height*width)
        this.data = when (raw) {
            is ArrayList -> raw
            else -> { ArrayList(raw)}
        }
        this.width = width
        this.height = height
    }

    val rect: Rect
        get() {
            return Rect(Coord(0,0), Coord(width-1,height-1))
        }

    private fun c2Idx(x: Int, y: Int): Int {
        return y * width + x
    }

    private fun c2Idx(c: Coord): Int {
        return c.y * width + c.x
    }

    operator fun get(x: Int, y: Int): T {
        return data[c2Idx(x, y)]
    }

    operator fun get(c: Coord): T {
        return data[c2Idx(c.x, c.y)]
    }

    /** Extract a sub array */
    operator fun get(r:Rect): Array2D<T> {
        val newHeight = r.height
        val newWidth = r.width
        val raw = buildList<T> {
            r.yRange.map { y ->
                r.xRange.map { x ->
                    add(this@Array2D[x, y])
                }
            }
        }
        return Array2D(raw, newWidth, newHeight)
    }

    operator fun set(x: Int, y: Int, value: T) {
        data[c2Idx(x, y)] = value
    }

    operator fun set(c: Coord, value: T) {
        data[c2Idx(c.x, c.y)] = value
    }

    /** Sets all elements in a rectangular grid to the same value */
    operator fun set(r:Rect, value: T) {
        r.yRange.forEach { y ->
            r.xRange.forEach { x ->
                data[c2Idx(x, y)] = value
            }
        }
    }

    fun modifyArea(r: Rect, mod: (T)->T) {
        r.yRange.forEach { y ->
            r.xRange.forEach { x ->
                data[c2Idx(x, y)] = mod(data[c2Idx(x, y)])
            }
        }
    }

    operator fun contains(c: Coord): Boolean {
        return c.x >= 0 && c.y >= 0 && c.x < width && c.y < height
    }

    fun onEdge(c: Coord): Boolean {
        return (c.x == 0 || c.y == 0 || c.x == this.width - 1 || c.y == this.height - 1)
    }

    fun neighbourCoords(x: Int, y: Int): List<Coord> {
        return STEPS_WITH_DIAG
            .map { (dx, dy) -> Coord(dx + x, dy + y) }
            .filter {
                this.contains(it)
            }
    }

    fun neighbours(x: Int, y: Int): Map<Coord, T> {
        return neighbourCoords(x, y).associateWith { coordinate -> get(coordinate) }
    }

    fun <R> map(function: (T) -> R): Array2D<R> {
        return Array2D(data.map(function), width, height)
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

    fun show(renderer: (T) -> String) : String {
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


    companion object {
        val STEPS_WITH_DIAG: List<Coord> =
            listOf(Coord(1, 1), Coord(1, 0), Coord(1, -1), Coord(0, 1), Coord(0, -1), Coord(-1, 1), Coord(-1, 0), Coord(-1, -1))

        fun <T> parseFromLines(string: String, charParser: (Char) -> T): Array2D<T> {
            return Array2D(
                string.nonEmptyLines().map { line ->
                    line.map(charParser)
                }
            )
        }

        fun a2renderBool(b: Boolean): String = if (b) "█" else " "
        fun a2renderInt(b: Int): String = b.toString()

    }
}
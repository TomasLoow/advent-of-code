package utils

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
    private fun c2Idx(x: Int, y: Int): Int {
        return y * width + x
    }

    private fun c2Idx(c: Coord): Int {
        return c2Idx(c.first, c.second)
    }

    operator fun get(x: Int, y: Int): T {
        return data[c2Idx(x, y)]
    }

    operator fun contains(c: Coord): Boolean {
        return c.first >= 0 && c.second >= 0 && c.first < width && c.second < height
    }

    fun onEdge(c: Coord): Boolean {
        return (c.first == 0 || c.second == 0 || c.first == this.width - 1 || c.second == this.height - 1)
    }

    operator fun get(c: Coord): T {
        return data[c2Idx(c.first, c.second)]
    }

    fun neighbourCoords(x: Int, y: Int): List<Coord> {
        return STEPS_WITH_DIAG
            .map { (dx, dy) -> Pair(dx + x, dy + y) }
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
                    add(function(Pair(x, y), this@Array2D[x, y]))
                }
            }
        }
    }

    fun countIndexedByCoordinate(function: (Coord, T) -> Boolean): Int {
        var counter = 0
        repeat(height) { y ->
            repeat(width) { x ->
                if (function(Pair(x, y), this[x, y])) counter++
            }
        }
        return counter
    }

    fun print(renderer: (T) -> String) {
        repeat(height) { y ->
            repeat(width) { x ->
                print(renderer(this[x,y]))
            }
            println()
        }
    }

    companion object {
        val STEPS_WITH_DIAG: List<Coord> =
            listOf(Pair(1, 1), Pair(1, 0), Pair(1, -1), Pair(0, 1), Pair(0, -1), Pair(-1, 1), Pair(-1, 0), Pair(-1, -1))

        fun <T> parseFromLines(string: String, charParser: (Char) -> T): Array2D<T> {
            return Array2D(
                string.nonEmptyLines().map { line ->
                    line.map(charParser)
                }
            )
        }
    }
}

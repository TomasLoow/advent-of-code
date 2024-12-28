package aoc.utils.geometry

open class Cursor<T : Any>(private val map: Array2D<T>, coord: Coord) {
    protected var x: Int
    protected var y: Int
    protected var idx: Int
    protected var prevIdx: Int? = null
    protected val h: Int
    protected val w: Int

    init {
        x = coord.x
        y = coord.y
        idx = map.c2Idx(coord)
        h = map.height
        w = map.width
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

    open fun moveRight(): Boolean {
        if (x == w - 1) {
            return false
        }
        x += 1
        prevIdx = idx
        idx += 1
        return true
    }

    open fun moveLeft(): Boolean {
        if (x == 0) {
            return false
        } else {
            x -= 1
            prevIdx = idx
            idx -= 1
            return true
        }
    }

    open fun moveDown(): Boolean {
        if (y == h - 1) {
            return false
        }
        y += 1
        prevIdx = idx
        idx += w
        return true
    }

    open fun moveUp(): Boolean {
        if (y == 0) {
            return false
        }
        y -= 1
        prevIdx = idx
        idx -= w
        return true
    }

    open fun moveUpRight(): Boolean {
        if (y == 0 || x == w - 1) return false
        y -= 1
        x += 1
        prevIdx = idx
        idx -= (w - 1)
        return true
    }

    open fun moveUpLeft(): Boolean {
        if (y == 0 || x == 0) return false
        y -= 1
        x -= 1
        prevIdx = idx
        idx -= (w + 1)
        return true
    }

    open fun moveDownRight(): Boolean {
        if (y == h - 1 || x == w - 1) return false
        y += 1
        x += 1
        prevIdx = idx
        idx += (w + 1)
        return true
    }

    open fun moveDownLeft(): Boolean {
        if (y == h - 1 || x == 0) return false
        y += 1
        x -= 1
        prevIdx = idx
        idx += (w - 1)
        return true
    }

    fun move(dir: Direction): Boolean {
        return when (dir) {
            Direction.UP -> moveUp()
            Direction.DOWN -> moveDown()
            Direction.LEFT -> moveLeft()
            Direction.RIGHT -> moveRight()
            Direction.UPRIGHT -> moveUpRight()
            Direction.UPLEFT -> moveUpLeft()
            Direction.DOWNRIGHT -> moveDownRight()
            Direction.DOWNLEFT -> moveDownLeft()
        }
    }

    fun moveTo(pos: Coord) {
        this.x = pos.x
        this.y = pos.y
        this.idx = map.c2Idx(pos)
    }

    fun back() {
        this.x = prev.x
        this.y = prev.y
        this.idx = prevIdx!!
        prevIdx = null
    }
}

class WrappingCursor<T : Any>(private val map: Array2D<T>, coord: Coord) : Cursor<T>(map, coord) {
    override fun moveRight(): Boolean {
        if (x == w - 1) {
            x = 0
            idx = map.c2Idx(x, y)
            return true
        }
        x += 1
        prevIdx = idx
        idx += 1
        return true
    }

    override fun moveLeft(): Boolean {
        if (x == 0) {
            x = w - 1
            idx = map.c2Idx(x, y)
            return true
        }
        x -= 1
        prevIdx = idx
        idx -= 1
        return true
    }

    override fun moveDown(): Boolean {
        if (y == h - 1) {
            y = 0
            idx = map.c2Idx(x, y)
            return true

        }
        y += 1
        prevIdx = idx
        idx += w
        return true
    }

    override fun moveUp(): Boolean {
        if (y == 0) {
            y = h - 1
            idx = map.c2Idx(x, y)
            return true
        }
        y -= 1
        prevIdx = idx
        idx -= w
        return true
    }

    override fun moveUpRight(): Boolean {
        TODO()
    }

    override fun moveUpLeft(): Boolean {
        TODO()
    }

    override fun moveDownRight(): Boolean {
        TODO()
    }

    override fun moveDownLeft(): Boolean {
        TODO()
    }
}
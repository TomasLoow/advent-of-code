package aoc.year2024

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.nonEmptyLines
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Direction
import aoc.utils.geometry.decomposeDirs
import aoc.utils.parseDirectionFromArrow
import kotlin.time.ExperimentalTime

class Day21Problem : DailyProblem<Long>() {

    override val number = 21
    override val year = 2024
    override val name = "Keypad Conundrum"

    private lateinit var goalNumbers: List<String>

    override fun commonParts() {
        goalNumbers = getInputText().nonEmptyLines()
    }

    override fun part1(): Long {

        return goalNumbers.sumOf { line ->
            val target = solveNumPad(line)
            val x = line.filter { it != 'A' }.toInt()
            val sol = solveN(target, 2)
            x * sol
        }
    }

    override fun part2(): Long {
        return goalNumbers.sumOf { line ->
            val target = solveNumPad(line)
            val x = line.filter { it != 'A' }.toInt()
            val sol = solveN(target, 25)
            x * sol
        }
    }

    private fun solveNumPad(first: String): String {
        val np = NumPad()
        val p: List<Action> = np.solve(first)
        return p.renderActions()
    }

    private fun solveN(code: String, times: Int = 25): Long {

        val goal = parseDirButtons(code)
        val divided = splitDirPresses(goal)
        assert(divided.joinToString("") == goal.renderDBs())
        val dp = DirPad()
        var chunkCounts = buildMap {
            divided.distinct().forEach { chunk ->
                put(chunk, divided.count { it == chunk }.toLong())
            }
        }

        repeat(times) {
            // if (idx > 1 ) dp.special = true
            val r = chunkCounts.map { (chunk, amount) ->
                dp.solve(chunk).renderActions() to amount
            }

            chunkCounts = buildMap {
                r.forEach { (chunk, amount) ->
                    splitDirPresses(parseDirButtons(chunk)).forEach { newChunk ->
                        if (newChunk in this) {
                            this[newChunk] = this[newChunk]!! + amount
                        } else {
                            this[newChunk] = amount
                        }
                    }
                }
            }
        }
        return (chunkCounts.toList().sumOf { (st, c) -> c * st.length.toLong() })
    }


    private fun splitDirPresses(points: List<DirButton>): List<String> {
        val sb = StringBuilder(points.renderDBs())

        return buildList {
            while (sb.isNotEmpty()) {
                val pre = sb.takeWhile { it != 'A' }
                sb.delete(0, pre.length + 1)
                add(pre.toString() + "A")
            }
        }
    }
}


data class NumButton(val i: Int?) {
    fun isA(): Boolean = i == null

    companion object {
        val a = NumButton(null)
    }
}

data class DirButton(val dir: Direction?) {
    fun isEnter(): Boolean = dir == null

    companion object {
        val enter = DirButton(null)
    }

    fun toChar(): Char {
        return if (isEnter()) 'A'
        else dir!!.toArrowChar()
    }

}


data class Action(val dir: Direction?) {
    fun isPress(): Boolean = dir == null

    companion object {
        val press = Action(null)
    }
}

abstract class Pad<A> {
    abstract val padMap: MutableMap<Coord, A>
    abstract val start: A
    abstract val void: Coord
    private val cache1 = emptyMutableMap<Pair<A, A>, List<Action>>()
    private val cache2 = emptyMutableMap<String, List<Action>>()


    abstract fun parseA(c: Char): A
    abstract fun renderA(l: A): Char
    open fun parseAs(s: String): List<A> = s.map { parseA(it) }
    open fun renderAs(l: List<A>): String = l.joinToString("") { renderA(it).toString() }

    /**
     * Returns the shortest paths from `from` to `to`
     */
    open fun solve(from: A, to: A): List<Action> {
        if ((from to to) in cache1) return cache1[(from to to)]!!
        val posStart = padMap.filterValues { it == from }.keys.first()
        val posEnd = padMap.filterValues { it == to }.keys.first()
        val stepsToTake = (posEnd - posStart).decomposeDirs()
        var res: List<Action>?
        if (stepsToTake.isEmpty()) res = listOf()
        else if (stepsToTake.size == 1) res = buildList {
            val count = stepsToTake.values.first()
            val dir = stepsToTake.keys.first()
            repeat(count) { add(Action(dir)) }
        }
        else {
            val dirH = stepsToTake.toList().first { it.first == Direction.RIGHT || it.first == Direction.LEFT }.first
            val dirV = stepsToTake.toList().first { it.first == Direction.UP || it.first == Direction.DOWN }.first
            val countH = stepsToTake[dirH]!!
            val countV = stepsToTake[dirV]!!
            val verticalFirst = (1..countV).map { Action(dirV) } + (1..countH).map { Action(dirH) }
            val horizontalFirst = (1..countH).map { Action(dirH) } + (1..countV).map { Action(dirV) }

            res = if (void == Coord(posStart.x, posEnd.y)) {
                horizontalFirst
            } else if (void == Coord(posEnd.x, posStart.y)) {
                verticalFirst
            } else {
                if (dirH == Direction.LEFT && dirV == Direction.DOWN) {
                    horizontalFirst
                } else if (dirH == Direction.RIGHT && dirV == Direction.UP) {
                    verticalFirst
                } else if (dirH == Direction.LEFT && dirV == Direction.UP) {
                    horizontalFirst
                } else {
                    verticalFirst
                }
            }
        }
        if (res == listOf(Action(Direction.DOWN), Action(Direction.LEFT), Action(Direction.LEFT))) {
            res = listOf(Action(Direction.LEFT), Action(Direction.DOWN), Action(Direction.LEFT))
        }

        cache1[(from to to)] = res
        return res
    }

    fun solve(s: String): List<Action> {
        val points = listOf(start) + parseAs(s)
        return solve(points)
    }

    fun solve(points: List<A>): List<Action> {
        val ps = renderAs(points)
        if (ps in cache2) {
            return cache2[ps]!!
        }

        if (points.size == 1) return emptyList()
        val firstParts = solve(points[0], points[1])
        val restParts = solve(points.drop(1))
        val res = firstParts + listOf(Action.press) + restParts
        cache2[ps] = res
        return res
    }

}

class NumPad : Pad<NumButton>() {
    override val start = NumButton.a

    override fun parseA(c: Char): NumButton {
        return if (c == 'A') NumButton.a else NumButton(c.toString().toInt())
    }

    override fun renderA(l: NumButton): Char {
        return if (l.isA()) 'A' else l.i.toString()[0]
    }

    override val void = numPadVoid
    override val padMap = numPad
}

class DirPad : Pad<DirButton>() {
    override val void = dirPadVoid
    override val padMap = dirPad
    override val start = DirButton.enter

    override fun parseA(c: Char): DirButton {
        return if (c == 'A') DirButton.enter else DirButton(parseDirectionFromArrow(c))
    }

    override fun renderA(l: DirButton): Char {
        return if (l.isEnter()) 'A' else l.dir!!.toArrowChar()
    }

    private val moveMap = buildMap {
        put('A' to '^', "<")
        put('A' to '>', "v")
        put('A' to 'v', "<v")
        put('A' to '<', "v<<")
        put('A' to 'A', "")

        put('^' to 'A', ">")
        put('^' to '^', "")
        put('^' to '<', "v<")
        put('^' to 'v', "v")
        put('^' to '>', "v>")

        put('>' to '>', "")
        put('>' to 'A', "^")
        put('>' to '^', "<^")
        put('>' to 'v', "<")
        put('>' to '<', "<<")

        put('v' to 'v', "")
        put('v' to 'A', "^>")
        put('v' to '>', ">")
        put('v' to '^', "^")
        put('v' to '<', "<")

        put('<' to '<', "")
        put('<' to 'A', ">>^")
        put('<' to '^', ">^")
        put('<' to 'v', ">")
        put('<' to '>', ">>")
    }

    override fun solve(from: DirButton, to: DirButton): List<Action> {
        return parseActions(moveMap[from.toChar() to to.toChar()]!!)
    }

}


/*
    +---+---+
    | ^ | A |
+---+---+---+
| < | v | > |
+---+---+---+
 */
val dirPad = mutableMapOf(
    Coord(1, 0) to DirButton(Direction.UP),
    Coord(2, 0) to DirButton.enter,
    Coord(0, 1) to DirButton(Direction.LEFT),
    Coord(1, 1) to DirButton(Direction.DOWN),
    Coord(2, 1) to DirButton(Direction.RIGHT),
)
val dirPadVoid = Coord(0, 0)

val numPad = mutableMapOf(
    Coord(0, 0) to NumButton(7),
    Coord(1, 0) to NumButton(8),
    Coord(2, 0) to NumButton(9),
    Coord(0, 1) to NumButton(4),
    Coord(1, 1) to NumButton(5),
    Coord(2, 1) to NumButton(6),
    Coord(0, 2) to NumButton(1),
    Coord(1, 2) to NumButton(2),
    Coord(2, 2) to NumButton(3),
    Coord(1, 3) to NumButton(0),
    Coord(2, 3) to NumButton.a,
)
val numPadVoid = Coord(0, 3)


private fun List<DirButton>.renderDBs(): String {
    return this.map { if (it.isEnter()) 'A' else it.dir!!.toArrowChar() }.joinToString("")
}

fun List<Action>.renderActions(): String {
    return this.map { if (it.isPress()) 'A' else it.dir!!.toArrowChar() }.joinToString("")
}


private fun parseActions(code: String): List<Action> {
    return code.map { c ->
        if (c == 'A') Action.press else Action(parseDirectionFromArrow(c))
    }
}

private fun parseDirButtons(code: String): List<DirButton> {
    return code.map { c ->
        if (c == 'A') DirButton.enter else DirButton(parseDirectionFromArrow(c))
    }
}


val day21Problem = Day21Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day21Problem.testData = true
    day21Problem.runBoth(1)
}
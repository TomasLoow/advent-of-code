package aoc.year2024

import DailyProblem
import aoc.utils.*
import kotlin.time.ExperimentalTime

data class NumpadState<Out>(val pos: Coord, val yielded: List<Out>) {

}

data class NumPress(val i: Int?) {
    fun isEnter(): Boolean = i == null
}

data class DirPress(val dir: Direction?) {
    fun isEnter(): Boolean = dir == null
}

abstract class Pad<A> {
    abstract fun press(press: DirPress): Pair<Pad<A>,A?>
}

data class NumPad(val pos:Coord): Pad<NumPress>() {
    val rect = numPadRect
    val void = numPadVoid
    val padMap = numPad

     override fun press(press: DirPress): Pair<NumPad, NumPress?> {
        if (press.isEnter()) {
            val r = padMap[pos]!!
            return this to r
        }
        val newPos = pos + press.dir!!
        if (newPos !in rect || newPos == void) throw OutOfKeyPadError()
        return copy(pos=newPos) to null
    }
}

data class DirPad(val pos:Coord): Pad<DirPress>() {
    val rect = dirPadRect
    val void = dirPadVoid
    val padMap = dirPad

    override fun press(press: DirPress): Pair<DirPad,DirPress?> {
        if (press.isEnter()) {
            val r = padMap[pos]!!
            return this to r
        }
        val newPos = pos + press.dir!!
        if (newPos !in rect || newPos == void) throw OutOfKeyPadError()
        return copy(pos=newPos) to null
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
    Coord(1, 0) to DirPress(Direction.UP),
    Coord(2, 0) to DirPress(null),
    Coord(0, 1) to DirPress(Direction.LEFT),
    Coord(1, 1) to DirPress(Direction.DOWN),
    Coord(2, 1) to DirPress(Direction.RIGHT),
)
val dirPadStart = dirPad.toList().first { it.second.isEnter() }.first
val dirPadVoid = Coord(0, 0)
val dirPadRect = Rect.bounding(dirPad.keys)

val numPad = mutableMapOf(
    Coord(0, 0) to NumPress(7),
    Coord(1, 0) to NumPress(8),
    Coord(2, 0) to NumPress(9),
    Coord(0, 1) to NumPress(4),
    Coord(1, 1) to NumPress(5),
    Coord(2, 1) to NumPress(6),
    Coord(0, 2) to NumPress(1),
    Coord(1, 2) to NumPress(2),
    Coord(2, 2) to NumPress(3),
    Coord(1, 3) to NumPress(0),
    Coord(2, 3) to NumPress(null),
)
val numPadStart = Coord(2, 3)
val numPadVoid = Coord(0, 3)
val numPadRect = Rect.bounding(numPad.keys)

data class FullState(val dp1: DirPad, val dp2: DirPad, val np: NumPad, val yielded: List<NumPress>) {
    fun press(userInput:DirPress): FullState {
        var newDp1 = dp1
        var newDp2 = dp2
        var newNp = np
        var newYielded: List<NumPress> = yielded
        val dp1Res = dp1.press(userInput)
        newDp1 = dp1Res.first
        val inputForDp2 = dp1Res.second
        if (inputForDp2 != null) {
            val dp2Res = dp2.press(dp1Res.second!!)
            newDp2 = dp2Res.first
            val inputForNp = dp2Res.second
            if (inputForNp != null) {
                val npRes = np.press(dp2Res.second!!)
                newNp = npRes.first
                if (npRes.second != null) {
                    newYielded = newYielded + npRes.second!!
                }
            }
        }
        return FullState(newDp1, newDp2, newNp, newYielded)
    }
}

class PadStar(goal: List<NumPress>): AStar<FullState>(FullState(DirPad(dirPadStart), DirPad(dirPadStart), NumPad(numPadStart), goal)) {

    override fun isGoal(state: FullState): Boolean {
        return state.yielded == goal.yielded
    }

    override fun heuristic(state: FullState): Int {
        return 0
    }

    override fun reachable(state: FullState): Collection<FullState> {
        val allPressess = Direction.cartesian.map { DirPress(it) } + DirPress(null)
        return buildList {
            allPressess.forEach {
                try {
                    val next = state.press(it)
                    if (next.yielded == goal.yielded.take(next.yielded.size)) add(next)
                } catch (e: OutOfKeyPadError) {

                }
            }
        }
    }

    override fun getMoveCost(from: FullState, to: FullState): Int {
        return 1
    }

}

class Day21Problem : DailyProblem<Int>() {

    override val number = 21
    override val year = 2024
    override val name = "Keypad Conundrum"

    private lateinit var data: List<List<NumPress>>

    fun parseActionLine(line: String): List<NumPress> {
        return line.map { c -> if (c == 'A') NumPress(null) else NumPress(c.toString().toInt()) }
    }

    override fun commonParts() {
        data = getInputText().lines().map { line ->
            parseActionLine(line)
        }
    }

    fun solve(code: String) : Int{
        val goal = code.map { c ->
            if (c =='A') NumPress(null) else NumPress(c.toString().toInt())
        }
        val codeVal = code.filter { it.isDigit() }.toInt()
        var solLen = 0
        var npPos = numPadStart
        for (n in goal) {
            val solver = PadStar(listOf(n))
            val s = solver.solve(FullState(DirPad(dirPadStart), DirPad(dirPadStart), NumPad(npPos), listOf()))
            solLen += s.first
            npPos = s.second.last().np.pos
        }
        return solLen*codeVal

    }

    override fun part1(): Int {
        return getInputText().nonEmptyLines().map { line -> solve(line) }.sum()
    }


    override fun part2(): Int {
        return 1
    }
}

class OutOfKeyPadError : Throwable()

val day21Problem = Day21Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day21Problem.testData = false
    day21Problem.runBoth(100)
}
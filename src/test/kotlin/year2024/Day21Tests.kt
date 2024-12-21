package year2024

import aoc.utils.Coord
import aoc.utils.Rect
import aoc.utils.parseDirectionFromArrow
import aoc.year2024.*
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Tests {


    @Test
    fun `test FullState`() {
        val presses = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".map { c ->
            if (c == 'A') DirPress(
                null
            ) else DirPress(parseDirectionFromArrow(c))
        }
        val fs = FullState(DirPad(dirPadStart), DirPad(dirPadStart), NumPad(numPadStart), emptyList())
        val end = presses.fold(fs) { acc, press ->
            acc.press(press)
        }
        println(end)
    }

    @Test
    fun `test FullState thows if first OOB`() {
        val presses = "<<<<<".map { c ->
            if (c == 'A') DirPress(
                null
            ) else DirPress(parseDirectionFromArrow(c))
        }
        val fs = FullState(DirPad(dirPadStart), DirPad(dirPadStart), NumPad(numPadStart), emptyList())
        try {
            presses.fold(fs) { acc, press ->
                acc.press(press)
            }
            assertTrue(false)
        } catch (e: OutOfKeyPadError) {

        }
    }

    @Test
    fun `test FullState thows if second OOB`() {
        val presses = "v<<AAAAAAA".map { c ->
            if (c == 'A') DirPress(
                null
            ) else DirPress(parseDirectionFromArrow(c))
        }
        val fs = FullState(DirPad(dirPadStart), DirPad(dirPadStart), NumPad(numPadStart), emptyList())
        try {
            presses.fold(fs) { acc, press ->
                acc.press(press)
            }
            assertTrue(false)
        } catch (e: OutOfKeyPadError) {

        }
    }

    @Test
    fun `test solve Part1 example`() {
        val p = Day21Problem()
        assertEquals(68 * 29, p.solve("029A"))
    }

}
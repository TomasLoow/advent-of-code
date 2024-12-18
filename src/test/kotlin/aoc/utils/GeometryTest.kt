package aoc.utils

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class GeometryTest {
    @Test
    fun `test vector arithmetics`() {
        val c1 = Coord(10, 10)
        val c2 = Coord(7, 11)
        val v = c1 - c2
        assertEquals(3 to -1, v)
        assertEquals(6 to -2, v + v)
        assertEquals(2 * v, v + v)
        assertEquals(2 * v, v * 2)
    }

    @Test
    fun `test bounding`() {
        val c1 = Coord(10, 10)
        val rSimple = Rect.bounding(listOf(c1))
        assertEquals(c1, rSimple.topLeft)
        assertEquals(c1, rSimple.bottomRight)
        assertEquals(c1, rSimple.topRight)
        assertEquals(c1, rSimple.bottomLeft)

        val rDuo = Rect.bounding(listOf(Coord(0, 10), Coord(10, 0)))
        assertEquals(Coord(0, 0), rDuo.topLeft)
        assertEquals(Coord(10, 10), rDuo.bottomRight)
        assertEquals(Coord(10, 0), rDuo.topRight)
        assertEquals(Coord(0, 10), rDuo.bottomLeft)

    }
}
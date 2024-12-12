package aoc.utils

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class GeometryTest {
    @Test
    fun `test vector arithmetics`() {
        val c1 = Coord(10,10)
        val c2 = Coord(7,11)
        val v = c1 - c2
        assertEquals(3 to -1, v)
        assertEquals(6 to -2, v + v)
        assertEquals(2*v, v + v)
        assertEquals(2*v, v*2)
    }
}
package aoc.utils

import aoc.utils.geometry.Array2D
import aoc.utils.geometry.Coord
import aoc.utils.geometry.Rect
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertTrue

class Array2DTest {

    @Test
    fun `test construct with lambda`() {
        val arr = Array2D(10, 10) { (x, y) ->
            (x + y) % 10
        }
        val expected = """
            0123456789
            1234567890
            2345678901
            3456789012
            4567890123
            5678901234
            6789012345
            7890123456
            8901234567
            9012345678

        """.trimIndent()
        assertEquals(expected, arr.show { Array2D.renderInt(it) })
        val arrB = Array2D(2, 2) { (x, y) ->
            2 * x + 3 * y + 1
        }
        val expectedB = """
            13
            46

        """.trimIndent()
        assertEquals(expectedB, arrB.show { Array2D.renderInt(it) })
    }

    @Test
    fun `test get and set`() {
        val arr = Array2D(listOf(listOf(false, false), listOf(false, false)))
        assertEquals(false, arr[0, 0])
        arr[0, 0] = true
        assertEquals(true, arr[0, 0])
        assertEquals(false, arr[1, 1])
        arr[1, 1] = !arr[1, 1]
        assertEquals(true, arr[1, 1])
    }

    @Test
    fun `test set and modify rectangle`() {
        val arr = Array2D(listOf(listOf(0, 0, 0), listOf(0, 0, 0), listOf(0, 0, 0)))

        arr[Rect(Coord.origin, Coord(1, 1))] = 1
        /*
        110
        110
        000
         */
        assertEquals(4, arr.mapAndFilterToListByNotNull { c, i -> i }.sum())
        assertEquals(1, arr[0, 0])
        assertEquals(1, arr[1, 0])
        assertEquals(1, arr[0, 1])
        assertEquals(1, arr[1, 1])
        arr.modifyArea(Rect(Coord(0, 1), Coord(2, 2))) { it + 1 }
        /*
        110
        221
        111
         */
        assertEquals(10, arr.mapAndFilterToListByNotNull { c, i -> i }.sum())
        assertEquals(1, arr[0, 0])
        assertEquals(2, arr[1, 1])
        assertEquals(1, arr[2, 2])


    }

    @Test
    fun `test neighbourCoords`() {
        val a2 = Array2D(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(2, 3, 4, 5),
                listOf(3, 4, 5, 6),
                listOf(4, 5, 6, 7)
            )
        )

        assertEquals(
            setOf(
                Coord(1, 1),
                Coord(2, 1),
                Coord(3, 1),
                Coord(1, 2),
                Coord(3, 2),
                Coord(1, 3),
                Coord(2, 3),
                Coord(3, 3),
            ), a2.neighbourCoords(Coord(2, 2), true).toSet(), "Correct coords for internal points"
        )
        assertEquals(
            setOf(
                Coord(2, 2), Coord(2, 3), Coord(1, 2), Coord(0, 3), Coord(0, 2)
            ), a2.neighbourCoords(Coord(1, 3), true).toSet(), "Correct coords for edge points"
        )
        assertEquals(
            setOf(
                Coord(0, 1), Coord(1, 1), Coord(1, 0)
            ), a2.neighbourCoords(Coord.origin, true).toSet(), "Correct coords for corner point"
        )
    }

    @Test
    fun `test neighbours`() {
        val a2 = Array2D(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(2, 3, 4, 5),
                listOf(3, 4, 5, 6),
                listOf(4, 5, 6, 7)
            )
        )
        val n = a2.neighbourCoordsAndValues(Coord(1, 1))
        assertEquals(1, n[Coord(0, 0)])
        assertEquals(2, n[Coord(1, 0)])
        assertEquals(3, n[Coord(2, 0)])
        assertEquals(2, n[Coord(0, 1)])
        assertEquals(4, n[Coord(2, 1)])
        assertEquals(3, n[Coord(0, 2)])
        assertEquals(4, n[Coord(1, 2)])
        assertEquals(5, n[Coord(2, 2)])
    }

    @Test
    fun `test parseFromLines`() {
        val a1 = Array2D.parseFromLines(
            """
                123
                112
                222
            """.trimIndent()
        ) { c -> c == '1' }
        assertEquals(3, a1.countIndexedByCoordinate { c, b -> b })
        val a2 = Array2D.parseFromLines(
            """
                ##.#
                #..#
                #.##
            """.trimIndent()
        ) { c -> c == '#' }
        assertEquals(false, a2[1, 1])
        assertEquals(true, a2[3, 2])
    }

    @Test
    fun `test contains`() {
        val a2 = Array2D(
            listOf(
                listOf(1, 2, 3, 4),
                listOf(2, 3, 4, 5),
            )
        )
        assertEquals(true, Coord(3, 0) in a2)
        assertEquals(true, Coord(1, 0) in a2)
        assertEquals(false, Coord(-2, 1) in a2)
        assertEquals(false, Coord(4, 1) in a2)
    }

    @Test
    fun testMap() {
        val intArray = Array2D.parseFromLines(
            """
                123
                149
                187
            """.trimIndent()
        ) { c -> c.digitToInt() }
        val boolArray = intArray.map { i -> i < 5 }
        assertEquals(intArray.width, boolArray.width)
        assertEquals(intArray.height, boolArray.height)
        assertEquals(true, boolArray[0, 0])
        assertEquals(true, boolArray[1, 0])
        assertEquals(true, boolArray[2, 0])
        assertEquals(true, boolArray[0, 1])
        assertEquals(true, boolArray[1, 1])
        assertEquals(true, boolArray[0, 2])
        assertEquals(6, boolArray.countIndexedByCoordinate { c, b -> b })
    }


    @Test
    fun testShiftDown() {
        val intArray = Array2D.parseFromLines(
            """
                123
                149
                187
            """.trimIndent()
        ) { c -> c.digitToInt() }

        intArray.shiftDown(1, 0)
        val res = intArray.show { Array2D.renderInt(it) }
        assertEquals(
            """
            000
            123
            149
            
        """.trimIndent(), res
        )

        intArray.shiftDown(1, 0)
        val res2 = intArray.show { Array2D.renderInt(it) }
        assertEquals(
            """
            000
            000
            123
            
        """.trimIndent(), res2
        )


    }

    @Test
    fun testShiftUp() {
        val intArray = Array2D.parseFromLines(
            """
                123
                149
                187
            """.trimIndent()
        ) { c -> c.digitToInt() }

        intArray.shiftUp(1, 0)
        val res = intArray.show { Array2D.renderInt(it) }
        assertEquals(
            """
            149
            187
            000
            
        """.trimIndent(), res
        )

        intArray.shiftUp(1, 0)
        val res2 = intArray.show { Array2D.renderInt(it) }
        assertEquals(
            """
            187
            000
            000
            
        """.trimIndent(), res2
        )
    }

    @Test
    fun testCursor() {

        val arr = Array2D(5, 5, 0)

        val c = arr.cursor(Coord(0, 0))

        assertEquals(0, c.value)
        assertEquals(Coord(0, 0), c.coord)
        c.set(5)
        assertEquals(5, c.value)

        c.moveRight(); c.set(4)
        c.moveDown(); c.set(5)
        c.moveLeft(); c.set(6)
        c.moveUp(); c.set(8)

        assertEquals("""
            84000
            65000
            00000
            00000
            00000
            
        """.trimIndent(), arr.show { Array2D.renderInt(it) })
        assertEquals(c.prev, Coord(0, 1))

        val a2 = Array2D(3, 3, 0)
        val c2 = a2.cursor(Coord(0, 1))
        c2.set(1); c2.moveUpRight()
        c2.set(2); c2.moveDownRight()
        c2.set(3); c2.moveDownLeft()
        c2.set(4); c2.moveUpLeft()
        assertEquals("""
            020
            103
            040
            
        """.trimIndent(), a2.show { Array2D.renderInt(it) })
        assertEquals(c2.coord, Coord(0, 1))
        assertEquals(c2.prev, Coord(1, 2))

    }

    @Test
    fun testCoordWithin() {

        val arr = Array2D(10, 10, 0)

        assertEquals(setOf(Coord(0,0), Coord(0,1), Coord(1,0)), arr.coordsWithin(Coord(0,0), 1).toSet())
        assertEquals(13, arr.coordsWithin(Coord(4,4), 2).size)
        assertEquals(100, arr.coordsWithin(Coord(7,4), 20).size)
    }

    @Test
    fun `test closest`() {
        val arr = Array2D(10, 10) { (x, y) ->
            (x + y) % 10
        }

        val c = arr.closest(Coord(3,0)) { c,v -> v == 5 }
        assertTrue(c in listOf<Coord>(Coord(5,0), Coord(4,1), Coord(3,2)))
        assertEquals(5, arr[c!!])
    }
}

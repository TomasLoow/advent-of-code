package utils

import aoc.utils.Array2D
import aoc.utils.Coord
import aoc.utils.Rect
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class Array2DTest {

    @Test
    fun `test get and set`() {
        val arr = Array2D<Boolean>(listOf(listOf(false,false), listOf(false,false)))
        assertEquals(false, arr[0,0])
        arr[0,0]=true
        assertEquals(true, arr[0,0])
        assertEquals(false, arr[1,1])
        arr[1,1]=!arr[1,1]
        assertEquals(true, arr[1,1])
    }

    @Test
    fun `test set and modify rectangle`() {
        val arr = Array2D(listOf(listOf(0,0,0),listOf(0,0,0),listOf(0,0,0)))

        arr[Rect(Coord.origin,Coord(1,1))]=1
        /*
        110
        110
        000
         */
        assertEquals(4, arr.mapListIndexedByCoordinate { c, i -> i }.sum())
        assertEquals(1, arr[0,0])
        assertEquals(1, arr[1,0])
        assertEquals(1, arr[0,1])
        assertEquals(1, arr[1,1])
        arr.modifyArea(Rect(Coord(0,1),Coord(2,2))) { it + 1 }
        /*
        110
        221
        111
         */
        assertEquals(10, arr.mapListIndexedByCoordinate { c, i -> i }.sum())
        assertEquals(1, arr[0,0])
        assertEquals(2, arr[1,1])
        assertEquals(1, arr[2,2])


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
            setOf<Coord>(
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
            setOf<Coord>(
                Coord(2, 2), Coord(2, 3), Coord(1, 2), Coord(0, 3), Coord(0, 2)
            ), a2.neighbourCoords(Coord(1,3), true).toSet(), "Correct coords for edge points"
        )
        assertEquals(
            setOf<Coord>(
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
        val n = a2.neighbours(Coord(1, 1))
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
        assertEquals(true, boolArray[0,0])
        assertEquals(true, boolArray[1,0])
        assertEquals(true, boolArray[2,0])
        assertEquals(true, boolArray[0,1])
        assertEquals(true, boolArray[1,1])
        assertEquals(true, boolArray[0,2])
        assertEquals(6, boolArray.countIndexedByCoordinate { c, b -> b })
    }
}
package aoc.year2024

import kotlin.test.Test
import kotlin.test.assertEquals

class Day21Tests {
    @Test
    fun `test 375A`() {
        val np = NumPad()
        val lv1 = np.solve("375A").renderActions()
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals(70, lv3.length)
    }

    @Test
    fun `test 2`() {
        val np = NumPad()
        val lv1 = np.solve("2").renderActions()
        assertEquals("<^A", lv1)
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        assertEquals("v<<A>^A>A", lv2)
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals("<vA<AA>>^AvA<^A>AvA^A", lv3)
    }

    @Test
    fun `test 080A`() {
        val np = NumPad()
        val lv1 = np.solve("080A").renderActions()
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals(60, lv3.length)
    }

    @Test
    fun `test 894A`() {
        val np = NumPad()
        val lv1 = np.solve("894A").renderActions()
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals(78, lv3.length)
    }

    @Test
    fun `test 159A`() {
        val np = NumPad()
        val lv1 = np.solve("159A").renderActions()
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals(82, lv3.length)
    }

    @Test
    fun `test 613A`() {
        val np = NumPad()
        val lv1 = np.solve("613A").renderActions()
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals(62, lv3.length)
    }

    @Test
    fun `test 029A`() {
        val np = NumPad()
        val lv1 = np.solve("029A").renderActions()
        assertEquals("<A^A^^>AvvvA", lv1)
        val dp = DirPad()
        val lv2 = dp.solve(lv1).renderActions()
        assertEquals("v<<A>>^A<A>A<AAv>A^A<vAAA^>A", lv2)
        val lv3 = dp.solve(lv2).renderActions()
        assertEquals("<vA<AA>>^AvAA<^A>Av<<A>>^AvA^Av<<A>>^AA<vA>A^A<A>Av<<A>A^>AAA<Av>A^A", lv3)
        val lv4 = dp.solve(lv3).renderActions()
        assertEquals(164, lv4.length)
        val lv5 = dp.solve(lv4).renderActions()
        assertEquals(404, lv5.length)
    }
}
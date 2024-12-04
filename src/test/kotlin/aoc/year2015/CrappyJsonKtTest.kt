package aoc.year2015

import aoc.year2015.crappyJSON.JSON
import aoc.year2015.crappyJSON.parseJSON
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrappyJsonKtTest {

    @Test
    fun `parseJSON works with ints`() {
        val (resParseInt, unparsed) = parseJSON("12345")
        assertTrue { unparsed.isEmpty() }
        assertTrue { resParseInt is JSON.I && resParseInt.int == 12345 }
        val (resParseNegInt, unparsed2) = parseJSON("-8")
        assertTrue { resParseNegInt is JSON.I && resParseNegInt.int == -8 }
        assertTrue { unparsed2.isEmpty() }
        val (_, unparsed3) = parseJSON("-8, 7")
        assertEquals(", 7", unparsed3)
    }

    @Test
    fun `parseJSON works with strings`() {
        val (resParse, unparsed) = parseJSON("\"hej\"")
        assertTrue { unparsed.isEmpty() }
        assertTrue { resParse is JSON.S && resParse.string == "hej" }
    }

    @Test
    fun `parseJSON works with int array`() {
        val (resParse, unparsed) = parseJSON("[1,2,3,4]")
        assertTrue { resParse is JSON.A && resParse.content.map { (it as JSON.I).int } == listOf(1, 2, 3, 4) }
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with string array`() {
        val (resParse, unparsed) = parseJSON("[\"a\",\"b\"]")
        assertTrue { resParse is JSON.A && resParse.content.map { (it as JSON.S).string } == listOf("a", "b") }
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with nested array`() {
        val (resParse, unparsed) = parseJSON("[[1],[2,3]]")
        assertTrue { resParse is JSON.A }
        val resParseJsonA = resParse as JSON.A
        val firstSubArray = resParseJsonA.content[0] as JSON.A
        val secondSubArray = resParseJsonA.content[1] as JSON.A
        assertTrue { (firstSubArray.content[0] as JSON.I).int == 1 }
        assertTrue { (secondSubArray.content[0] as JSON.I).int == 2 }
        assertTrue { (secondSubArray.content[1] as JSON.I).int == 3 }

        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with objects`() {
        val (resParse, unparsed) = parseJSON("""{"a":1,"b":"foo","c":[1,2,3]}""")
        assertEquals("foo", ((resParse as JSON.O).content["b"] as JSON.S).string)
        assertEquals(2, ((resParse.content["c"] as JSON.A).content[1] as JSON.I).int)
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with recursive objects`() {
        val (resParse, unparsed) = parseJSON("""{"a":{"b":1}}""")
        assertEquals(1, (((resParse as JSON.O).content["a"] as JSON.O).content["b"] as JSON.I).int)
        assertTrue { unparsed.isEmpty() }
    }

}
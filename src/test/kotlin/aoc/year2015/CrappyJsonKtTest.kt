package aoc.year2015

import aoc.year2015.crappyJSON.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrappyJsonKtTest {

    @Test
    fun `parseJSON works with ints`() {
        val (resParseInt, unparsed) = parseJSON("12345")
        assertTrue { unparsed.isEmpty() }
        assertTrue { resParseInt is JSONInt && resParseInt.int == 12345 }
        val (resParseNegInt, unparsed2) = parseJSON("-8")
        assertTrue { resParseNegInt is JSONInt && resParseNegInt.int == -8 }
        assertTrue { unparsed2.isEmpty() }
        val (_, unparsed3) = parseJSON("-8, 7")
        assertEquals(", 7", unparsed3)
    }

    @Test
    fun `parseJSON works with strings`() {
        val (resParse, unparsed) = parseJSON("\"hej\"")
        assertTrue { unparsed.isEmpty() }
        assertTrue { resParse is JSONString && resParse.string == "hej" }
    }

    @Test
    fun `parseJSON works with int array`() {
        val (resParse, unparsed) = parseJSON("[1,2,3,4]")
        assertTrue { resParse is JSONArray && resParse.content.map { (it as JSONInt).int } == listOf(1, 2, 3, 4) }
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with string array`() {
        val (resParse, unparsed) = parseJSON("[\"a\",\"b\"]")
        assertTrue { resParse is JSONArray && resParse.content.map { (it as JSONString).string } == listOf("a", "b") }
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with nested array`() {
        val (resParse, unparsed) = parseJSON("[[1],[2,3]]")
        assertTrue { resParse is JSONArray }
        val firstSubArray = (resParse as JSONArray).content[0] as JSONArray
        val secondSubArray = (resParse as JSONArray).content[1] as JSONArray
        assertTrue { (firstSubArray.content[0] as JSONInt).int == 1 }
        assertTrue { (secondSubArray.content[0] as JSONInt).int == 2 }
        assertTrue { (secondSubArray.content[1] as JSONInt).int == 3 }

        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with objects`() {
        val (resParse, unparsed) = parseJSON("""{"a":1,"b":"foo","c":[1,2,3]}""")
        assertEquals("foo", ((resParse as JSONObject).content["b"] as JSONString).string)
        assertEquals(2, (((resParse as JSONObject).content["c"] as JSONArray).content[1] as JSONInt).int)
        assertTrue { unparsed.isEmpty() }
    }

    @Test
    fun `parseJSON works with recursive objects`() {
        val (resParse, unparsed) = parseJSON("""{"a":{"b":1}}""")
        assertEquals(1, (((resParse as JSONObject).content["a"] as JSONObject).content["b"] as JSONInt).int)
        assertTrue { unparsed.isEmpty() }
    }

}
package aoc.year2020

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PTest {

    @Test
    fun `Constant match - matching character`() {
        val constantRule = P.Constant('a')
        val result = constantRule.match("abc") { throw UnsupportedOperationException() }
        assertEquals(listOf(1), result, "Expected to match and consume the first character 'a'")
        val result2 = constantRule.match("bbaabc") { throw UnsupportedOperationException() }
        assertEquals(emptyList(), result2, "Expected to not match anything")
    }

    @Test
    fun `Concat match`() {
        val rulesMap: (Int) -> P = { id ->
            when (id) {
                1 -> P.Constant('a')
                2 -> P.Constant('b')
                else -> throw UnsupportedOperationException()
            }
        }
        val concatRule = P.Concat(listOf(1, 2, 2))
        val result = concatRule.match("abb", rulesMap)
        assertEquals(listOf(3), result, "Expected to match and consume the full sequence 'abb'")
        val result2 = concatRule.match("ab", rulesMap)
        assertTrue(result2.isEmpty(), "Expected no matches when the sequence is incomplete")
    }


    @Test
    fun `Or match - first rule matches`() {
        val rulesMap: (Int) -> P = { id ->
            when (id) {
                1 -> P.Constant('a')
                2 -> P.Constant('b')
                else -> throw UnsupportedOperationException()
            }
        }
        val orRule = P.Or(listOf(1), listOf(2))
        val result = orRule.match("abc", rulesMap)
        assertEquals(listOf(1), result, "Expected the first rule to match and consume 'a'")
        val result2 = orRule.match("bac", rulesMap)
        assertEquals(listOf(1), result2, "Expected the second rule to match and consume 'b'")
        val result3 = orRule.match("cde", rulesMap)
        assertTrue(result3.isEmpty(), "Expected no matches")
    }

    @Test
    fun `Complex rule with Concat and Or`() {
        val rulesMap: (Int) -> P = { id ->
            when (id) {
                1 -> P.Constant('a')
                2 -> P.Constant('b')
                3 -> P.Or(listOf(1), listOf(2))
                else -> throw UnsupportedOperationException()
            }
        }
        val complexRule = P.Concat(listOf(3, 1))
        val result = complexRule.match("aa", rulesMap)
        assertEquals(listOf(2), result, "Expected to match 'aa' using the complex rule with Concat and Or")
        val result2 = complexRule.match("ab", rulesMap)
        assertTrue(result2.isEmpty(), "Expected no matches as the second character does not match")
    }


    @Test
    fun `Plus matches single occurrence of rule`() {
        val ruleMap: (Int) -> P = { P.Constant('a') }
        val plus = P.Plus(1)
        val result = plus.match("a", ruleMap)
        assertEquals(listOf(1), result, "Expected to match single 'a'")
        val resultMany = plus.match("aaab", ruleMap)
        assertTrue(resultMany.contains(1), "Expected to match the first 'a'")
        assertTrue(resultMany.contains(2), "Expected to match the first two 'a's")
        assertTrue(resultMany.contains(3), "Expected to match all three 'a's")
        val resultFail = plus.match("b", ruleMap)
        assertTrue(resultFail.isEmpty(), "Expected no matches for 'b'")
        val resultFail2 = plus.match("", ruleMap)
        assertTrue(resultFail2.isEmpty(), "Expected no matches for empty")
    }


    @Test
    fun `Plus with more complex sub rule`() {
        val ruleMap = { id: Int ->
            when (id) {
                1 -> P.Constant('a')
                2 -> P.Constant('b')
                3 -> P.Concat(listOf(1, 1)) // "aa"
                4 -> P.Or(listOf(3), listOf(2)) // "aa"|"b"
                else -> throw UnsupportedOperationException()
            }
        }
        val plus = P.Plus(4) // ("aa"|"b")+
        val result = plus.match("aab", ruleMap)
        assertTrue(result.contains(2), "Expected to match the first 'aa'")
        assertTrue(result.contains(3), "Expected to match the the full string")
        val result2 = plus.match("baaab", ruleMap)
        assertEquals(listOf(1, 3), result2, "Expected to match b, baa, but not baaab")
    }

    @Test
    fun matching() {
        val ruleMap = { id: Int ->
            when (id) {
                1 -> P.Constant('a')
                2 -> P.Constant('b')
                else -> throw UnsupportedOperationException()
            }
        }
        val plus = P.Matching(1, 2) // a...ab...b
        val result = plus.match("ab", ruleMap)
        assertEquals(listOf(2), result, "Expected to match the full string")
        val result2 = plus.match("aaabbb", ruleMap)
        assertEquals(listOf(6), result2, "Expected to match the full string")
        val resultFail = plus.match("aaabb", ruleMap)
        assertEquals(emptyList(), resultFail, "Expected to match the full string")
    }
}
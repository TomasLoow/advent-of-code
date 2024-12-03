package aoc.utils

import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class DotsDisplayKtTest {

    @Test
    fun testParse() {
        val input = """
              ██  ███   ██  ███  ████ ████  ██  █  █ ███    ██ █  █ █    █  █ █  █  ██  ███   ██  ███   ███ █  █ ████ ██████   █
             █  █ █  █ █  █ █  █ █    █    █  █ █  █  █      █ █ █  █    ████ ██ █ █  █ █  █ █  █ █  █ █    █  █    █   █  █   █
             █  █ ███  █    █  █ ███  ███  █    ████  █      █ ██   █    ████ █ ██ █  █ █  █ █  █ █  █ █    █  █   █    █   █ █ 
             ████ █  █ █    █  █ █    █    █ ██ █  █  █      █ █ █  █    █  █ █  █ █  █ ███  █  █ ███   ██  █  █  █     █    █  
             █  █ █  █ █  █ █  █ █    █    █  █ █  █  █   █  █ █ █  █    █  █ █  █ █  █ █    █ █  █ █     █ █  █ █      █    █  
             █  █ ███   ██  ███  ████ █     ███ █  █ ███   ██  █  █ ████ █  █ █  █  ██  █     █ █ █  █ ███   ██  ████   █    █  
        """.trimIndent()
        val res = parseDisplay(input)
        assertEquals("ABCDEFGHIJKLMNOPQRSUZTY", res)
    }
}
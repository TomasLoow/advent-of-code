package aoc.utils

import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

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
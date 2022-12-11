package aoc.utils

val dispToCharTable = mapOf(
    """
    ███  
    █  █ 
    █  █ 
    ███  
    █ █  
    █  █ 
    
""".trimIndent() to 'R',
    """
    ████ 
    █    
    ███  
    █    
    █    
    █    
    
""".trimIndent() to 'F',
    """
    █  █ 
    █ █  
    ██   
    █ █  
    █ █  
    █  █ 
    
""".trimIndent() to 'K',
    """
    ████ 
       █ 
      █  
     █   
    █    
    ████ 
    
""".trimIndent() to 'Z',
    """
     ██  
    █  █ 
    █    
    █    
    █  █ 
     ██  
    
""".trimIndent() to 'C',
    """
    ███  
    █  █ 
    █  █ 
    ███  
    █    
    █    
    
""".trimIndent() to 'P',
    """
    ████ 
    █    
    ███  
    █    
    █    
    ████ 
    
""".trimIndent() to 'E',
    """
    ████ 
    █    
    ███  
    █    
    █    
    █    
    
""".trimIndent() to 'F',
    """
    █  █ 
    █  █ 
    ████ 
    █  █ 
    █  █ 
    █  █ 
    
    """.trimIndent() to 'H',
    """
     ██  
    █  █ 
    █  █ 
    ████ 
    █  █ 
    █  █ 
    
    """.trimIndent() to 'A',
    """
      ██ 
       █ 
       █ 
       █ 
    █  █ 
     ██  
    
    """.trimIndent() to 'J',
    """
    ███  
    █  █ 
    ███  
    █  █ 
    █  █ 
    ███  
    
    """.trimIndent() to 'B',
    """
    ███  
    █  █ 
    █  █ 
    █  █ 
    █  █ 
    ███  
    
    """.trimIndent() to 'D',
    """
     ██  
    █  █ 
    █    
    █ ██ 
    █  █ 
     ███ 
    
    """.trimIndent() to 'G',
    """
    ███  
     █   
     █   
     █   
     █   
    ███  
    
    """.trimIndent() to 'I',
    """
    █    
    █    
    █    
    █    
    █    
    ████ 
    
    """.trimIndent() to 'L',
    """
    █  █ 
    ████ 
    ████ 
    █  █ 
    █  █ 
    █  █ 
    
    """.trimIndent() to 'M',
    """
    █  █ 
    ██ █ 
    █ ██ 
    █  █ 
    █  █ 
    █  █ 
    
    """.trimIndent() to 'N',
    """
     ██  
    █  █ 
    █  █ 
    █  █ 
    █  █ 
     ██  
    
    """.trimIndent() to 'O',
    """
     ██  
    █  █ 
    █  █ 
    █  █ 
    █ █  
     █ █ 
    
    """.trimIndent() to 'Q',
    """
     ███ 
    █    
    █    
     ██  
       █ 
    ███  
    
    """.trimIndent() to 'S',
    """
    █  █ 
    █  █ 
    █  █ 
    █  █ 
    █  █ 
     ██  
    
    """.trimIndent() to 'U',
    """
    █████
      █  
      █  
      █  
      █  
      █  
    
    """.trimIndent() to 'T',
    """
    █   █
    █   █
     █ █ 
      █  
      █  
      █  
    
    """.trimIndent() to 'Y'
)
val charToDispTable = dispToCharTable.map { (k,v) -> Pair(v,k) }.toMap()


fun parseDisplay(display: String): String {
    println(charToDispTable.keys.sorted())
    val width = display.filter{ c -> c != '\n'}.length / 6
    val array = Array2D<Boolean>(display.filter{ c -> c != '\n'}.map { it == '█' }, width = width, height = 6)
    val numChars = width/5
    val charSegments = (0 until  numChars).map { i ->
        array[Rect(Coord(5 * i, 0), Coord(5 * i + 4, 5))].show { Array2D.a2renderBool(it) }
    }
    return charSegments.map {
        if (it !in dispToCharTable) {
            println("=== Unknown char ===")
            println(it)
            println("======")
            throw Exception("Failed to parse display")
        }
        dispToCharTable[it]
    }.joinToString("")
}
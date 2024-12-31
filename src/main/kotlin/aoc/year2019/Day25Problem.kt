package aoc.year2019

import DailyProblem
import aoc.utils.BFS
import aoc.utils.emptyMutableMap
import aoc.utils.extensionFunctions.subSets
import aoc.utils.geometry.Direction
import aoc.utils.parseAllDigits
import aoc.utils.parseIntCodeComputer
import kotlin.time.ExperimentalTime

val badObjects = listOf(
    "giant electromagnet",
    "infinite loop",
    "photons",
    "molten lava",
    "shell",
    "escape pod"
)

data class Room(val name: String, val exits : MutableMap<Direction, String?> = emptyMutableMap(), val items : MutableList<String> = mutableListOf())

fun strToDir(s: String) = when(s) {
    "north" -> Direction.UP
    "south" -> Direction.DOWN
    "east" -> Direction.RIGHT
    "west" -> Direction.LEFT
    else -> throw IllegalArgumentException("Unknown direction $s")
}

fun dirToStr(d: Direction) = when(d) {
    Direction.UP -> "north"
    Direction.DOWN -> "south"
    Direction.RIGHT -> "east"
    Direction.LEFT -> "west"
    else -> throw IllegalArgumentException("Invalid direction for problem: $d")
}

data class Explorer(var currentRoom: Room, var lastMoveDirection: Direction? = null, val items: MutableList<String>, val map: MutableMap<String, Room>)

class Day25Problem : DailyProblem<Int>() {

    override val number = 25
    override val year = 2019
    override val name = "Cryostasis"

    private lateinit var computer: IntCode
    var verbose: Boolean = false

    override fun commonParts() {
        computer = parseIntCodeComputer(getInputText())
    }

    fun p(s:String) = if (verbose) println(s) else {}

    fun parseRoom(outputString: String): Room {
        val lines = outputString.lines()
        val name = lines.first { "==" in it }.dropWhile { it in "= " }.dropLastWhile { it in "= " }
        val doors = lines.dropWhile { "Doors here" !in it }.takeWhile { it.isNotEmpty() }.drop(1).map { it.removePrefix("- ") }.map { strToDir(it) }
        val items = lines.dropWhile { "Items here" !in it }.takeWhile { it.isNotEmpty() }.drop(1).map { it.removePrefix("- ") }
        return Room(name, exits = doors.associateWith { null }.toMutableMap(), items = items.toMutableList())
    }

    private fun manualGame() {
        while (true) {
            runTillInput()
            userInput()
        }
    }

    private fun runTillInput() {
        val o = computer.runUntilNeedsInputOrHalt()
        val outputString = o.output.map { it.toInt().toChar() }.joinToString("")
        print(outputString)
    }


    private fun userInput() {
        var stringInput = readLine()!!
        val shortCuts = mapOf(
            "s" to "south",
            "w" to "west",
            "n" to "north",
            "e" to "east",
            "t" to "take",
            "d" to "drop",
            "i" to "inv"
        )
        shortCuts.forEach { (k, v) ->
            if (stringInput == k || stringInput.startsWith(k + " ")) {
                stringInput = stringInput.replaceFirst(k, v)
            }
        }

        stringInput.map { computer.writeInput(it.code.toLong()) }
        computer.writeInput(10) // newline
    }

    fun exploreShip(): Explorer {
        val o = computer.runUntilNeedsInputOrHalt()
        val outputString = o.output.map { it.toInt().toChar() }.joinToString("")
        val startRoom = parseRoom(outputString)
        p("Entered ${startRoom.name}")
        val map = mutableMapOf(startRoom.name to startRoom)
        val explorer = Explorer(startRoom, null, mutableListOf(), map)
        var doneExploring = false
        while (!doneExploring) {

            explorer.currentRoom.items.filter { it !in badObjects }.forEach { item -> pickUp(item, explorer.currentRoom, explorer) }
            val d = explorer.lastMoveDirection?:(Direction.cartesian - explorer.currentRoom.exits.keys).first()
            val candidateDirs = listOf(d.rotateCW(), d, d.rotateCCW(), d.rotate180()) .filter { it in explorer.currentRoom.exits }// explore the ship in a "hand-on-right-wall" strategy
            var choosenDir = candidateDirs.first()
            if (explorer.currentRoom.name == "Security Checkpoint") {
               choosenDir = explorer.lastMoveDirection!!.rotate180() // leave for now
            }
            val newRoom:Room = if (explorer.currentRoom.exits[choosenDir] != null && explorer.currentRoom.exits[choosenDir]!! in map) {
                move(choosenDir, explorer)
                map[explorer.currentRoom.exits[choosenDir]!!]!!
            } else {
                val outputString = move(choosenDir, explorer)
                parseRoom(outputString)
            }
            if (newRoom.name !in map) {
                map[newRoom.name] = newRoom
            }
            explorer.currentRoom.exits[choosenDir] = newRoom.name
            newRoom.exits[choosenDir.rotate180()] = explorer.currentRoom.name
            explorer.currentRoom = newRoom
            explorer.lastMoveDirection=choosenDir
            p("Entered ${newRoom.name}")

            doneExploring = map.values.all { it.name == "Security Checkpoint" || it.exits.values.all { it != null }}
        }
        p("done exploring")
        return explorer
    }

    private fun pickUp(item: String, room: Room, explorer: Explorer) {
        "take $item".map { computer.writeInput(it.code.toLong()) }
        p("Picking up ${item}")
        computer.writeInput(10)
        explorer.items.add(item)
        room.items.remove(item)
        computer.runUntilNeedsInputOrHalt()

    }

    private fun move(dir:Direction, explorer: Explorer) : String{
        "${dirToStr(dir)}".map { computer.writeInput(it.code.toLong()) }
        computer.writeInput(10)
        val o = computer.runUntilNeedsInputOrHalt()
        return o.output.map { it.toInt().toChar() }.joinToString("")
    }

    private fun dropItem(item: String, room: Room, explorer: Explorer) {
        "drop $item".map { computer.writeInput(it.code.toLong()) }
        computer.writeInput(10)
        explorer.items.remove(item)
        room.items.add(item)
        computer.runUntilNeedsInputOrHalt()
        p("Dropped ${item}")

    }

    override fun part1(): Int {
        // uncomment for manual fun!
        // manualGame()
        val e = exploreShip()
        moveToSecurityCheckpoint(e)
        return getPastCheckpoint(e)
    }

    private fun getPastCheckpoint(e: Explorer): Int {
        val room = e.currentRoom
        val sensorsDir = (room.exits.keys - e.lastMoveDirection!!.rotate180()).toList().first()!!
        val allItems = e.items
        allItems.subSets().forEach { choice ->
            p("Trying combination: $choice")
            val itemsToDrop = e.items - choice
            val itemsToPickUp = choice - e.items
            itemsToDrop.toList().forEach { dropItem(it, room, e) }
            itemsToPickUp.forEach { pickUp(it, room, e) }
            val outStr = move(sensorsDir, e)
            if ("keypad" in outStr) {
                p(outStr)
                return parseAllDigits(outStr).first()
            }
        }
        return -1
    }

    private fun moveToSecurityCheckpoint(explorer: Explorer) {
        val shipMap = explorer.map
        val goal = shipMap["Security Checkpoint"]!!

        // find path with bfs
        class B: BFS<Room>(goal) {
            override fun reachable(state: Room): Collection<Room> {
                return state.exits.values.filter { it in shipMap }.map { shipMap[it]!! }
            }
        }
        val path = B().solve(explorer.currentRoom).map { it.name }.drop(1)
        p("path to checkpoint: $path")
        path.forEach { roomName ->
            val d = explorer.currentRoom.exits.filter { it.value == roomName }.keys.first()
            move(d, explorer)
            explorer.lastMoveDirection = d
            explorer.currentRoom = shipMap[roomName]!!
            p("Entered ${explorer.currentRoom.name}")
        }
    }

    override fun part2(): Int = 20191225 // Merry Christmas!
}

val day25Problem = Day25Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day25Problem.testData = false
    day25Problem.verbose = false
    day25Problem.runBoth(100)
}
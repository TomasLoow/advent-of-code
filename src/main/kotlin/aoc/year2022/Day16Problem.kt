package aoc.year2022

import DailyProblem
import aoc.utils.BFS
import aoc.utils.emptyMutableMap
import aoc.utils.parseListOfTriples
import aoc.utils.extensionFunctions.subSets
import java.lang.Integer.parseInt
import kotlin.time.ExperimentalTime

data class Room(val name: String, val flow: Int, val connections: List<String>)

class Day16Problem : DailyProblem<Int>() {

    override val number = 16
    override val year = 2022
    override val name = "Proboscidea Volcanium"
    private lateinit var roomMap: Map<String, Room>

    private lateinit var allRooms: Set<Room>
    private lateinit var roomsWithValves: Set<Room>

    private lateinit var distances: Map<String, MutableMap<String, Int>>

    override fun commonParts() {
        fun parseValve(s: String): String = s.substringAfter("Valve ")
        fun parseConnections(s: String): List<String> = s.split(", ")
        val rawData = parseListOfTriples(
            getInputText(),
            ::parseValve,
            ::parseInt,
            ::parseConnections,
            " has flow rate=".toRegex(),
            "; tunnels? leads? to valves? ".toRegex()
        )

        roomMap = (rawData
            .map { Pair(it.first, Room(it.first, it.second, it.third)) }).toMap()
        distances = buildMap {
            roomMap.keys.forEach {
                this[it] = emptyMutableMap()
            }
            class ShortestPath(goal: Room) : BFS<Room>(goal) {
                override fun reachable(state: Room): Collection<Room> {
                    return state.connections.map { roomMap[it]!! }
                }
            }
            roomMap.keys.forEach { from ->
                roomMap.keys.filter { to -> to <= from }.forEach { to ->
                    val shortest = ShortestPath(roomMap[to]!!).solve(roomMap[from]!!).size
                    this[from]!![to] = shortest - 1
                    this[to]!![from] = shortest - 1
                }
            }
        }
        allRooms = roomMap.values.toSet()
        roomsWithValves = allRooms.filter { it.flow > 0 }.toSet()
    }

    private fun distance(r1: String, r2: String): Int = distances[r1]!![r2]!!

    private val cache: MutableMap<Pair<List<String>, Int>, Int> = emptyMutableMap()
    private fun scoreRoute(route: List<String>, time: Int): Int {
        if (Pair(route, time) in cache) {
            return cache[Pair(route, time)]!!
        }
        val roomName = route.first()
        val room = roomMap[roomName]!!
        if (time <= 1) return room.flow * (time - 1)
        if (route.size == 1) return (time - 1) * room.flow
        val nextRoom = route[1]
        val d = distance(roomName, nextRoom)
        val options = listOf(
            room.flow * (time - 1) + scoreRoute(route.drop(1), time - d - 1), //Open
            scoreRoute(route.drop(1), time - d),  // No open

        )
        val res = options.maxOf { it }
        cache[Pair(route, time)] = res
        return res
    }

    private fun routesOfMaxLength(l: Int, rooms: Collection<String>): Sequence<List<String>> {
        val q = ArrayDeque<Pair<List<String>, Int>>()
        q.addLast(Pair(listOf("AA"), 0))
        return sequence {
            while (q.isNotEmpty()) {
                val (path, t) = q.removeFirst()
                yield(path)
                val current = path.last()
                rooms.filter { it !in path && distance(it, current) <= l-t }.forEach {
                    q.addLast(Pair(path+it,t+distance(it, current)))
                }
            }
        }
    }

    override fun part1(): Int {
        val maxTime = 30
        val cheekiness = 5

        val routes = routesOfMaxLength(maxTime - cheekiness, roomsWithValves.map { it.name })
        return routes.maxOf { scoreRoute(it, maxTime) }

    }

    override fun part2(): Int {
        val maxTime = 26
        val cheekiness = 5
        val subsets = roomsWithValves.map{it.name}.subSets().filter { it.size > 2 && it.size < roomsWithValves.size - 2 }
        val routePairs = subsets.map { ss ->
            val complement = roomsWithValves.map{it.name} - ss
            Pair(
                routesOfMaxLength(maxTime - cheekiness, ss).filter { it.size > 3 },
                routesOfMaxLength(maxTime - cheekiness, complement).filter { it.size > 3 }
            )
        }
        return routePairs.maxOf { (routes1, routes2) ->
            val val1 = routes1.maxOfOrNull { scoreRoute(it, maxTime) }?:0
            val val2 = routes2.maxOfOrNull { scoreRoute(it, maxTime) }?:0
            val1 + val2
        }
    }
}
val day16Problem = Day16Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day16Problem.testData = false
    day16Problem.runBoth(1)
}
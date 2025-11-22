package aoc.year2019

import aoc.DailyProblem
import aoc.utils.emptyMutableList
import aoc.utils.parseIntCodeProgram
import kotlin.time.ExperimentalTime

private class Packet(val x: Long, val y: Long)

private sealed interface NetworkDevice {
    fun receive(packet: Packet)
    fun run(network: MutableMap<Long, NetworkDevice>) = Unit
    val isIdle: Boolean

    data class Computer(
        private val computer: IntCode,
        private var id: Long,
        private val haltOn255: Boolean,
        private val inputQueue: MutableList<Packet> = emptyMutableList()
    ) : NetworkDevice {

        init {
            computer.writeInput(id)
        }

        override fun receive(packet: Packet) {
            inputQueue.add(packet)
        }

        override val isIdle: Boolean
            get() = inputQueue.isEmpty()

        override fun run(network: MutableMap<Long, NetworkDevice>) {
            if (inputQueue.isNotEmpty()) {
                val p = inputQueue.removeFirst()
                //println("${computer.name} input: $p")
                computer.writeInput(p.x)
                computer.writeInput(p.y)
            } else computer.writeInput(-1)
            val runRes = computer.runUntilNeedsInputOrHalt()

            runRes.output.windowed(3).forEach { (d, x, y) ->
                //println("${computer.name} output: $d, $x, $y")
                if (haltOn255 && d == 255L) throw NetworkDoneException(y)
                network[d]?.receive(Packet(x, y))
            }

        }
    }

    data class NAT(
        private var lastPacket: Packet? = null
    ) : NetworkDevice {
        private val sentToZero = mutableSetOf<Long>()

        override fun receive(packet: Packet) {
            lastPacket = packet
        }

        override val isIdle = true

        override fun run(network: MutableMap<Long, NetworkDevice>) {
            if (network.values.all { nc -> nc.isIdle }) {
                //println("Idle!")
                if (lastPacket?.y in sentToZero) throw NetworkDoneException(lastPacket!!.y)
                network[0]!!.receive(lastPacket ?: Packet(0, 0))
                sentToZero.add(lastPacket!!.y)
            }
        }
    }
}

private class NetworkDoneException(val result: Long) : Exception()

class Day23Problem : DailyProblem<Long>() {

    override val number = 23
    override val year = 2019
    override val name = "Category Six"

    private lateinit var code: Array<Long>
    private lateinit var network: MutableMap<Long, NetworkDevice>

    override fun commonParts() {
        code = parseIntCodeProgram(getInputText())
    }

    private fun initializeNetwork(haltOn255: Boolean): MutableMap<Long, NetworkDevice> =
        (0L..49L).associateWith {
            NetworkDevice.Computer(
                IntCode(code, name = it.toString()),
                id = it,
                haltOn255 = haltOn255
            )
        }.toMutableMap()

    override fun part1(): Long {
        network = initializeNetwork(haltOn255 = true)
        try {
            while (true) {
                network.values.forEach { nc ->
                    nc.run(network)
                }
            }
        } catch (e: NetworkDoneException) {
            return e.result
        }
    }

    override fun part2(): Long {
        network = initializeNetwork(haltOn255 = false)
        network[255L] = NetworkDevice.NAT()
        try {
            while (true) {
                network.values.forEach {  nc ->
                    nc.run(network)
                }
            }
        } catch (e: NetworkDoneException) {
            return e.result
        }
    }
}

val day23Problem = Day23Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day23Problem.testData = false
    day23Problem.runBoth(100)
}
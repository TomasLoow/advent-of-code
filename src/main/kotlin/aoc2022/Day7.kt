package aoc2022

import DailyProblem
import utils.*
import kotlin.time.ExperimentalTime

class Day7Problem(override val inputFilePath: String) : DailyProblem<Int> {
    private lateinit var fileSystem: Directory
    override val number = 7
    override val name = "No Space Left On Device"

    /* Commands and ls output */
    sealed class Cmd
    class CmdCd(var path: String) : Cmd()
    class CmdLs(var content: List<LsLine>) : Cmd()

    sealed class LsLine
    class LsFile(val name: String, val size: Int) : LsLine()
    class LsDir(val name: String) : LsLine()

    private fun parseCommands(): List<Cmd> {
        val inputLines = getInputFile().readNonEmptyLines().toMutableList()
        val commands = mutableListOf<Cmd>()
        while (inputLines.isNotEmpty()) {
            val command = inputLines.removeFirst()
            if (command.startsWith("$ cd ")) {
                commands.add(CmdCd(command.substringAfter("$ cd ")))
            } else {
                assert(command == "$ ls")
                val content = buildList {
                    while (inputLines.isNotEmpty() && !inputLines.first().startsWith("$")) {
                        val lsLine = inputLines.removeFirst()
                        val (desc, name) = lsLine.split(" ")
                        if (desc == "dir") {
                            add(LsDir(name))
                        } else {
                            add(LsFile(name, desc.toInt()))
                        }
                    }
                }
                commands.add(CmdLs(content))
            }
        }
        return commands
    }

    class Directory(
        val name: String,
        val subDirectories: MutableMap<String, Directory> = mutableMapOf(),
        val files: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val parent: Directory? = null
    ) {

        private var cachedSize: Int? = null
        val totalSize: Int
            get() {
                if (cachedSize != null) return cachedSize!!
                cachedSize = files.sumOf { it.second } + subDirectories.values.sumOf { it.totalSize }
                return cachedSize!!
            }

        val root: Directory
            get() {
                if (parent == null) return this
                return this.parent.root
            }

        fun findAllDirsWhere(predicate: (Directory) -> Boolean): List<Directory> {
            return buildList {
                if (predicate(this@Directory)) {
                    add(this@Directory)
                }
                subDirectories.values.forEach {
                    addAll(it.findAllDirsWhere(predicate))
                }
            }
        }
    }

    private fun buildRootDirectory(commands: List<Cmd>): Directory {
        fun handleCd(
            cmd: CmdCd,
            current: Directory
        ) = when (cmd.path) {
            ".." -> current.parent!!
            "/" -> current.root
            else -> current.subDirectories[cmd.path]!!
        }

        fun handleLs(cmd: CmdLs, current: Directory) {
            cmd.content.forEach { lsLine ->
                when (lsLine) {
                    is LsDir -> current.subDirectories[lsLine.name] = Directory(lsLine.name, parent = current)
                    is LsFile -> current.files.add(Pair(lsLine.name, lsLine.size))
                }
            }
        }

        val root = Directory("/")
        var current = root
        commands.forEach { cmd ->
            when (cmd) {
                is CmdCd -> current = handleCd(cmd, current)
                is CmdLs -> handleLs(cmd, current)
            }
        }
        return root
    }

    override fun commonParts() {
        val commands = parseCommands()
        fileSystem = buildRootDirectory(commands)

    }

    override fun part1(): Int {
        return fileSystem
            .findAllDirsWhere { dir -> dir.totalSize < 100000 }
            .sumOf { dir -> dir.totalSize }
    }

    override fun part2(): Int {
        val discCapacity = 70000000
        val neededFreeSpace = 30000000
        val maxDiskSpaceToUse = discCapacity - neededFreeSpace

        val currentlyUsed = fileSystem.totalSize
        val toBeDeleted = currentlyUsed - maxDiskSpaceToUse
        return fileSystem
            .findAllDirsWhere { dir -> dir.totalSize > toBeDeleted }
            .minOf { it.totalSize }
    }
}

val day7Problem = Day7Problem("input/aoc2022/day7.txt")

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.runBoth(1000)
}
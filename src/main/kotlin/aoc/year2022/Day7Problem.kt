package aoc.year2022

import DailyProblem
import aoc.utils.emptyMutableMap
import aoc.utils.emptyMutableSet
import aoc.utils.extensionFunctions.readNonEmptyLines
import kotlin.time.ExperimentalTime

class Day7Problem : DailyProblem<Int>() {
    private lateinit var fileSystem: Directory
    override val number = 7
    override val year = 2022
    override val name = "No Space Left On Device"

    /* Commands and ls output */
    sealed interface Cmd {
        class Cd(var path: String) : Cmd
        class Ls(var content: List<Day7Problem.Ls>) : Cmd
    }

    sealed interface Ls {
        class File(val name: String, val size: Int) : Ls
        class Dir(val name: String) : Ls
    }

    private fun parseCommands(): List<Cmd> {
        val inputLines = getInputFile().readNonEmptyLines().toMutableList()
        val commands = mutableListOf<Cmd>()
        while (inputLines.isNotEmpty()) {
            val command = inputLines.removeFirst()
            if (command.startsWith("$ cd ")) {
                commands.add(Cmd.Cd(command.substringAfter("$ cd ")))
            } else {
                check(command == "$ ls")
                val content = buildList {
                    while (inputLines.isNotEmpty() && !inputLines.first().startsWith("$")) {
                        val lsLine = inputLines.removeFirst()
                        val (desc, name) = lsLine.split(" ")
                        if (desc == "dir") {
                            add(Ls.Dir(name))
                        } else {
                            add(Ls.File(name, desc.toInt()))
                        }
                    }
                }
                commands.add(Cmd.Ls(content))
            }
        }
        return commands
    }

    class Directory(
        val name: String,
        val subDirectories: MutableMap<String, Directory> = emptyMutableMap(),
        val files: MutableSet<Pair<String, Int>> = emptyMutableSet(),
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
            cmd: Cmd.Cd,
            current: Directory
        ) = when (cmd.path) {
            ".." -> current.parent!!
            "/" -> current.root
            else -> current.subDirectories[cmd.path]!!
        }

        fun handleLs(cmd: Cmd.Ls, current: Directory) {
            cmd.content.forEach { lsLine ->
                when (lsLine) {
                    is Ls.Dir -> current.subDirectories[lsLine.name] = Directory(lsLine.name, parent = current)
                    is Ls.File -> current.files.add(Pair(lsLine.name, lsLine.size))
                }
            }
        }

        val root = Directory("/")
        var current = root
        commands.forEach { cmd ->
            when (cmd) {
                is Cmd.Cd -> current = handleCd(cmd, current)
                is Cmd.Ls -> handleLs(cmd, current)
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

val day7Problem = Day7Problem()

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.runBoth(1000)
}
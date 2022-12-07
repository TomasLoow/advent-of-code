package aoc2022

import DailyProblem
import utils.*
import java.io.File
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
        val inputLines = File(inputFilePath).readNonEmptyLines().toMutableList()
        val cmds = mutableListOf<Cmd>()
        while (inputLines.isNotEmpty()) {
            val cmd = inputLines.removeFirst()
            if (cmd.startsWith("$ cd")) {
                cmds.add(CmdCd(cmd.substring(5)))
            } else {
                assert(cmd.startsWith("$ ls"))
                val content = mutableListOf<LsLine>()
                while (inputLines.isNotEmpty() && !inputLines.first().startsWith("$")) {
                    val lsLine = inputLines.removeFirst()
                    val (desc, name) = lsLine.split(" ")
                    if (desc == "dir") {
                        content.add(LsDir(name))
                    } else {
                        content.add(LsFile(name, desc.toInt()))
                    }
                }
                cmds.add(CmdLs(content))
            }
        }
        return cmds
    }


    class Directory(
        val name: String,
        val subDirectories: MutableMap<String, Directory> = mutableMapOf(),
        val files: MutableSet<Pair<String, Int>> = mutableSetOf(),
        val parent: Directory? = null
    ) {

        var cachedSize: Int? = null
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

        fun forThisAndEachSubDir(function: (Directory) -> Unit) {
            function(this)
            this.subDirectories.values.forEach { subDir -> subDir.forThisAndEachSubDir(function) }
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
                is CmdCd -> {
                    current = handleCd(cmd, current)
                }

                is CmdLs -> {
                    handleLs(cmd, current)
                }
            }
        }
        return root
    }




    override fun commonParts() {
        val commands = parseCommands()
        fileSystem = buildRootDirectory(commands)

    }


    override fun part1(): Int {
        var s = 0
        fileSystem.forThisAndEachSubDir { dir: Directory ->
            if (dir.totalSize < 100000) {
                s += dir.totalSize
            }
        }
        return s
    }


    override fun part2(): Int {
        val DISK_CAPACITY = 70000000
        val FREE_SPACE_NEEDED = 30000000
        val MAX_TO_DISK_USAGE = DISK_CAPACITY - FREE_SPACE_NEEDED

        val currentlyUsed = fileSystem.totalSize
        val toBeDeleted = currentlyUsed - MAX_TO_DISK_USAGE
        var bestCandidate = fileSystem
        fileSystem.forThisAndEachSubDir { dir ->
            if (dir.totalSize > toBeDeleted && dir.totalSize < bestCandidate.totalSize) {
                bestCandidate = dir
            }
        }
        return bestCandidate.totalSize
    }
}

val day7Problem = Day7Problem("input/aoc2022/day7.txt")

@OptIn(ExperimentalTime::class)
fun main() {
    day7Problem.runBoth(100)
}
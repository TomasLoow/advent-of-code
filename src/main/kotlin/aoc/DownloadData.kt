package aoc

import aoc.utils.parseAllPositiveInts
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

suspend fun main() {

    val lines = File("input/downloadinfo.txt").readLines()

    val sessionId = lines.first()

    val dls: List<Triple<Int, Int, Int>> = lines.drop(1).map { line ->
        val (year, startDate, endDate) = parseAllPositiveInts(line)
        Triple(year, startDate, endDate)
    }

    HttpClient {
        defaultRequest {
            header(HttpHeaders.Cookie, "session=$sessionId")
            header(HttpHeaders.UserAgent, "Personal downloader script, Tomas Lööw (github.com/TomasLoow)")
            header("Script-Info", "Used to download personal input data, will only ever be run manually. Has a 10s delay between requests.")
        }
    }.use { client ->
        dls.forEach {(year, startDate, endDate) ->
            val dir = File("input/aoc$year")
            if (!dir.exists()) {
                dir.mkdir()
            }

            for (day in (startDate..endDate).reversed()) {
                val paddedDay = day.toString().padStart(2, '0')
                val fileName = "input/aoc$year/day$paddedDay.txt"
                val file = File(fileName)
                if (file.exists()) {
                    println("$fileName already exists, skipping.")
                    continue
                }
                val response: HttpResponse = client.get("https://adventofcode.com/$year/day/$day/input")
                val body = response.readRawBytes()
                file.writeBytes(body)
                println("$fileName downloaded.")
                Thread.sleep(10000)
            }
        }
    }
}
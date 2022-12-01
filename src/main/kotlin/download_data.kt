import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

suspend fun main() {

    val (sessionId, endDate) = File("input/aoc2022/downloadinfo.txt").readLines().let {
            Pair(it[0], it[1].toInt())
        }

    HttpClient {
        defaultRequest {
            header(HttpHeaders.Cookie, "session=$sessionId")
            header(HttpHeaders.UserAgent, "Personal downloader script, Tomas Lööw")
        }
    }.use { client ->
        for (day in 1..endDate) {
            val fileName = "input/aoc2022/day$day.txt"
            val file = File(fileName)
            if (file.exists()) {
                println("$fileName already exists, skipping.")
                continue
            }
            val response: HttpResponse = client.get("https://adventofcode.com/2022/day/$day/input")
            val body = response.readBytes()
            file.writeBytes(body)
            println("$fileName downloaded.")
        }
    }
}
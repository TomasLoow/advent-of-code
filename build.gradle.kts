plugins {
    kotlin("jvm") version "2.0.21"
    application
}


group = "info.loow.tomas"
version = "1.0-2024-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    // Ktor is used in DownloadData.kt to fetch personal input files
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-cio:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("aoc.MainKt")
}


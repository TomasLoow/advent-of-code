plugins {
    kotlin("jvm") version "2.2.21"
    application
}


group = "info.loow.tomas"
version = "1.0-2025-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.1")

    // Ktor is used in DownloadData.kt to fetch personal input files
    implementation("io.ktor:ktor-client-core:3.3.2")
    implementation("io.ktor:ktor-client-cio:3.3.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("aoc.MainKt")
}


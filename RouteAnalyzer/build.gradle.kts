plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"  // Aggiungi questa riga
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("com.charleskorn.kaml:kaml:0.53.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("RouteAnalyzer")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "RouteAnalyzerKt"
    }
}

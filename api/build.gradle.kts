plugins {
    kotlin("jvm") version "1.5.10"
    id("io.gitlab.arturbosch.detekt").version("1.19.0-RC2")
}

group = "net.javaman"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
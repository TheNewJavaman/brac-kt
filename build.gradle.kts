plugins {
    kotlin("multiplatform") version "1.6.0" apply false
    id("org.jetbrains.dokka") version "1.6.0"
}

repositories {
    mavenCentral()
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}

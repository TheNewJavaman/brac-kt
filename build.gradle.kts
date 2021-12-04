val detektVersion: String by project

plugins {
    kotlin("multiplatform") version "1.6.0" apply false
    kotlin("jvm") version "1.6.0" apply false
    kotlin("plugin.serialization") version "1.6.0" apply false
    id("org.jetbrains.dokka") version "1.6.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC2" apply false
    id("maven-publish")
}

subprojects {
    group = "net.javaman.brac-kt"
    version = "0.1.0"

    apply(plugin = "maven-publish")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    ext {
        set("jvmTarget", "11")
        set("datetimeVersion", "0.3.1")
        set("coroutinesVersion", "1.6.0-RC")
        set("ktorVersion", "1.6.5")
        set("jupiterVersion", "5.7.0")
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/TheNewJavaman/brac-kt")
                credentials {
                    username = System.getenv("GITHUB_USERNAME")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}

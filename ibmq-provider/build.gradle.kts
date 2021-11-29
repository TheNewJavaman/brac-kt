plugins {
    kotlin("multiplatform") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC2"
    id("org.jetbrains.dokka") version "1.6.0"
}

group = "net.javaman"
version = "0.1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        val test by compilations.getting {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":api"))
                implementation("io.ktor:ktor-client-core:1.6.5")
                implementation("io.ktor:ktor-client-cio:1.6.5")
                implementation("io.ktor:ktor-client-serialization:1.6.5")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.7.0")
            }
        }
    }
}

detekt {
    this.source = objects.fileCollection().from(
        "src/commonMain/kotlin",
        "src/jvmTest/kotlin"
    )
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))
}

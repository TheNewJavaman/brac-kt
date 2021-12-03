plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.6.0"
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
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
                jvmTarget = "11"
            }
        }
        val test by compilations.getting {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":api"))
                implementation("io.ktor:ktor-client-core:1.6.5")
                implementation("io.ktor:ktor-client-cio:1.6.5")
                implementation("io.ktor:ktor-client-serialization:1.6.5")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
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
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin"
    )
}

tasks.withType<Test>().getByName("jvmTest") {
    useJUnitPlatform()
}

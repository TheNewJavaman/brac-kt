plugins {
    kotlin("multiplatform")
    id("io.gitlab.arturbosch.detekt") version "1.19.0-RC2"
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
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC")
            }
        }
        val jvmMain by getting {}
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

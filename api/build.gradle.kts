plugins {
    kotlin("multiplatform") version "1.6.0"
    id("io.gitlab.arturbosch.detekt").version("1.19.0-RC2")
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
                implementation(kotlin("stdlib-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
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

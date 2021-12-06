plugins {
    kotlin("multiplatform")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val jvmTarget: String by ext
val datetimeVersion: String by ext
val jupiterVersion: String by ext

kotlin {
    jvm {
        val main by compilations.getting {
            kotlinOptions {
                jvmTarget = jvmTarget
            }
        }
        val test by compilations.getting {
            kotlinOptions {
                jvmTarget = jvmTarget
            }
        }
    }
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        nodejs {
            testTask {
                useMocha {}
            }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {}
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {}
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
        "src/commonTest/kotlin",
        "src/jsMain/kotlin",
        "src/jvmMain/kotlin"
    )
}

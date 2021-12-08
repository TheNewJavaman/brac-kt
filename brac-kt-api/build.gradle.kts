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
val ktorVersion: String by ext
val coroutinesVersion: String by ext
val coroutinesMtVersion: String by ext

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
        moduleName = "brac-kt-api"
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        nodejs {
            testTask {
                useMocha()
            }
        }
        compilations["main"].packageJson {
            customField("name" to "@thenewjavaman/brac-kt-api")
            customField("description" to "A Kotlin/Multiplatform interface for quantum computing")
            customField("homepage" to "https://github.com/TheNewJavaman/brac-kt")
            customField(
                "author" to mapOf(
                    "name" to "Gabriel Pizarro",
                    "email" to "gpizarro@javaman.net",
                    "url" to "https://javaman.net"
                )
            )
            customField(
                "repository" to mapOf(
                    "type" to "git",
                    "url" to "https://github.com/TheNewJavaman/brac-kt"
                )
            )
            customField("private" to false)
        }
        binaries.executable()
    }
    linuxX64 {
        binaries {
            sharedLib {
                baseName = "brac_kt_api"
            }
        }
    }
    mingwX64 {
        binaries {
            sharedLib {
                baseName = "brac_kt_api"
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
                api("io.ktor:ktor-client-core:$ktorVersion")
                api("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val linuxX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesMtVersion")
            }
        }
        val mingwX64Main by getting {
            dependencies {
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesMtVersion")
            }
        }
    }
}

detekt {
    this.source = objects.fileCollection().from(
        "src/commonMain/kotlin",
        "src/commonTest/kotlin",
        "src/jsMain/kotlin",
        "src/jvmMain/kotlin",
        "src/linuxX64Main/kotlin",
        "src/mingwX64Main/kotlin"
    )
}

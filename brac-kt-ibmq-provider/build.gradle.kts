plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

val jvmTarget: String by ext
val datetimeVersion: String by ext
val ktorVersion: String by ext
val jupiterVersion: String by ext
val coroutinesVersion: String by ext

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
        moduleName = "brac-kt-ibmq-provider"
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
        compilations["main"].packageJson {
            customField("name" to "@thenewjavaman/brac-kt-ibmq-provider")
            customField("description" to "A Kotlin/Multiplatform interface for quantum computing")
            customField("homepage" to "https://github.com/TheNewJavaman/brac-kt")
            customField("author" to mapOf(
                "name" to "Gabriel Pizarro",
                "email" to "gpizarro@javaman.net",
                "url" to "https://javaman.net"
            ))
            customField("repository" to mapOf(
                "type" to "git",
                "url" to "https://github.com/TheNewJavaman/brac-kt"
            ))
            customField("private" to false)
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":brac-kt-api"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
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
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
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

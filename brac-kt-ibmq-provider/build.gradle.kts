plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("maven-publish")
    id("io.gitlab.arturbosch.detekt")
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
                useMocha()
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
    linuxX64 {
        binaries {
            sharedLib {
                baseName = "brac_kt_ibmq_provider"
                export(project(":brac-kt-api"))
            }
        }
    }
    mingwX64 {
        binaries {
            sharedLib {
                baseName = "brac_kt_ibmq_provider"
                export(project(":brac-kt-api"))
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":brac-kt-api"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val linuxX64Main by getting
        val linuxX64Test by getting
        val mingwX64Main by getting
        val mingwX64Test by getting
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
        "src/jsTest/kotlin",
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin",
        "src/mingwX64Main/kotlin",
        "src/mingwX64Test/kotlin"
    )
}

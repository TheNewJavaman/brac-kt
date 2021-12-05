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
val coroutinesVersion: String by ext
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
            tasks.withType<Test> {
                useJUnitPlatform()
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }
        val jvmMain by getting {}
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
            }
        }
    }
}

publishing {
    publications.configureEach {
        this as MavenPublication
        artifactId = "brac-kt-$artifactId"
    }
}

detekt {
    this.source = objects.fileCollection().from(
        "src/commonMain/kotlin",
        "src/jvmMain/kotlin",
        "src/jvmTest/kotlin"
    )
}

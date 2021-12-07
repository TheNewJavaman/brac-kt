plugins {
    kotlin("js")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":brac-kt-ibmq-provider"))
}

kotlin {
    js(IR) {
        browser {}
        nodejs {}
        binaries.executable()
    }
}

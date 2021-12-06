plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":brac-kt-api"))
    implementation(project(":brac-kt-ibmq-provider"))
}

kotlin {
    js(IR) {
        browser {}
        nodejs {}
        binaries.executable()
    }
}

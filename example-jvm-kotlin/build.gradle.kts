plugins {
    kotlin("jvm")
    application
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":brac-kt-ibmq-provider"))
}

application {
    mainClass.set("Application")
}

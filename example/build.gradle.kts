plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api"))
    implementation(project(":ibmq-provider"))
}

application {
    mainClass.set("net.javaman.brackt.example.Application")
}

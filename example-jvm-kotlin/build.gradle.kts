plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("net.javaman.brac-kt:brac-kt-api:main-SNAPSHOT")
    implementation("net.javaman.brac-kt:brac-kt-ibmq-provider:main-SNAPSHOT")
}

application {
    mainClass.set("net.javaman.brackt.example.Application")
}

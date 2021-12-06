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
    implementation(project(":brac-kt-api"))
    implementation(project(":brac-kt-ibmq-provider"))
}

application {
    mainClass.set("net.javaman.brackt.example.ExampleApplication")
}

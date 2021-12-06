import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

val jvmTarget: String by ext

dependencies {
    implementation(project(":brac-kt-api"))
    implementation(project(":brac-kt-ibmq-provider"))
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = jvmTarget
    }
}

application {
    mainClass.set("Application")
}

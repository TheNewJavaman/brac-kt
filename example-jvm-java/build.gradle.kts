plugins {
    application
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

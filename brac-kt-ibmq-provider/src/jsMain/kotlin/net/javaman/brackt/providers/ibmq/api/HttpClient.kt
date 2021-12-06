package net.javaman.brackt.providers.ibmq.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

actual val client = HttpClient(Js) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

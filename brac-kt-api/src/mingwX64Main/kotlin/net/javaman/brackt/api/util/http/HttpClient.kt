package net.javaman.brackt.api.util.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.curl.Curl
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

actual val client = HttpClient(Curl) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

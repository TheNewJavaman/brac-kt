package net.javaman.brakt.providers.ibmq.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenRequest
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenResponse
import kotlin.jvm.JvmStatic

class IbmQApi private constructor() {
    companion object {
        private const val BASE_AUTH_URL = "https://auth.quantum-computing.ibm.com/api"
        private const val BASE_API_URL = "https://api.quantum-computing.ibm.com/api"

        private val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        @JvmStatic
        suspend fun logInWithToken(request: LogInWithTokenRequest): LogInWithTokenResponse {
            return client.post("$BASE_AUTH_URL/users/loginWithToken") {
                contentType(ContentType.Application.Json)
                body = request
            }
        }
    }
}

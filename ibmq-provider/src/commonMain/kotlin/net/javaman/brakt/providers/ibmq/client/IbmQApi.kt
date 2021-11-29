package net.javaman.brakt.providers.ibmq.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.javaman.brakt.providers.ibmq.client.models.GetApiTokenResponse
import net.javaman.brakt.providers.ibmq.client.models.GetNetworkResponse
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenRequest
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenResponse
import kotlin.jvm.JvmStatic

class IbmQApi private constructor() {
    companion object {
        private const val BASE_AUTH_URL = "https://auth.quantum-computing.ibm.com/api"
        private const val BASE_API_URL = "https://api.quantum-computing.ibm.com/api"
        private const val BASE_QCON_URL = "https://api-qcon.quantum-computing.ibm.com/api"
        private const val ACCESS_TOKEN_HEADER = "x-access-token"

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

        @JvmStatic
        suspend fun getApiToken(accessToken: String, userId: String): GetApiTokenResponse {
            return client.get("$BASE_AUTH_URL/users/$userId/apiToken") {
                header(ACCESS_TOKEN_HEADER, accessToken)
            }
        }

        @JvmStatic
        suspend fun getNetwork(accessToken: String): GetNetworkResponse {
            return client.get("$BASE_API_URL/network") {
                header(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
    }
}

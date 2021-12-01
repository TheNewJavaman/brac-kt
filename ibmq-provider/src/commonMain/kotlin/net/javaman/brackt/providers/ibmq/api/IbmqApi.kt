package net.javaman.brackt.providers.ibmq.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.javaman.brackt.providers.ibmq.api.models.ApiTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsLimitResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse

/**
 * An HTTP client for [IBM Quantum](https://quantum-computing.ibm.com/)
 */
class IbmqApi {
    companion object {
        private const val BASE_AUTH_URL = "https://auth.quantum-computing.ibm.com/api"
        private const val BASE_API_URL = "https://api.quantum-computing.ibm.com/api"
        private const val ACCESS_TOKEN_HEADER = "x-access-token"
    }

    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun logInWithToken(request: LogInWithTokenRequest): LogInWithTokenResponse {
        return client.post("$BASE_AUTH_URL/users/loginWithToken") {
            contentType(ContentType.Application.Json)
            body = request
        }
    }

    suspend fun apiToken(accessToken: String, userId: String): ApiTokenResponse {
        return client.get("$BASE_AUTH_URL/users/$userId/apiToken") {
            header(ACCESS_TOKEN_HEADER, accessToken)
        }
    }

    suspend fun networks(accessToken: String): NetworksResponse {
        return client.get("$BASE_API_URL/network") {
            header(ACCESS_TOKEN_HEADER, accessToken)
        }
    }

    suspend fun backends(accessToken: String): BackendsResponse {
        return client.get("$BASE_API_URL/users/backends") {
            header(ACCESS_TOKEN_HEADER, accessToken)
        }
    }

    suspend fun jobs(accessToken: String): JobsResponse {
        return client.get("$BASE_API_URL/jobs/v2") {
            header(ACCESS_TOKEN_HEADER, accessToken)
        }
    }

    suspend fun jobsLimit(
        accessToken: String,
        network: String,
        group: String,
        project: String,
        device: String
    ): JobsLimitResponse {
        return client.get("$BASE_API_URL/network/$network/groups/$group/projects/$project/devices/$device/jobsLimit") {
            header(ACCESS_TOKEN_HEADER, accessToken)
        }
    }

    suspend fun runExperiment(
        accessToken: String,
        request: RunExperimentRequest,
        network: String,
        group: String,
        project: String
    ): RunExperimentResponse {
        return client.post("$BASE_API_URL/network/$network/groups/$group/projects/$project/runExperiment") {
            header(ACCESS_TOKEN_HEADER, accessToken)
            contentType(ContentType.Application.Json)
            body = request
        }
    }

    suspend fun versions(accessToken: String, request: VersionsRequest, code: String): VersionsResponse {
        return client.post("$BASE_API_URL/codes/$code/versions/") {
            header(ACCESS_TOKEN_HEADER, accessToken)
            contentType(ContentType.Application.Json)
            body = request
        }
    }
}

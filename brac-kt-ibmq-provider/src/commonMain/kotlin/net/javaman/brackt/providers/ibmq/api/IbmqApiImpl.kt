package net.javaman.brackt.providers.ibmq.api

import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.javaman.brackt.api.util.formatters.censor
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.api.models.ApiTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.JobResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsLimitResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsResponse
import net.javaman.brackt.providers.ibmq.api.models.LastestResponse
import net.javaman.brackt.providers.ibmq.api.models.LatestResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.NewResponse
import net.javaman.brackt.providers.ibmq.api.models.ResultDownloadUrlResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse

/**
 * An HTTP client for [IBM Quantum](https://quantum-computing.ibm.com/)
 */
@Suppress("TooManyFunctions")
open class IbmqApiImpl {
    private val logger: Logger by injection()

    companion object {
        private const val BASE_AUTH_URL = "https://auth.quantum-computing.ibm.com/api"
        private const val BASE_API_URL = "https://api.quantum-computing.ibm.com/api"
        private const val ACCESS_TOKEN_HEADER = "x-access-token"
    }

    /**
     * Request an access (session) token using an API token
     */
    suspend fun logInWithToken(request: LogInWithTokenRequest): LogInWithTokenResponse {
        logger.info { "Requesting access token with apiToken (${request.apiToken.censor()})" }
        val response = client.post<LogInWithTokenResponse>("${BASE_AUTH_URL}/users/loginWithToken") {
            contentType(ContentType.Application.Json)
            body = request
        }
        logger.info { "Received accessToken (${response.id.censor()})" }
        return response
    }

    /**
     * Request the original API token that created this access token
     */
    suspend fun apiToken(accessToken: String, userId: String): ApiTokenResponse {
        logger.info { "Requesting API token with userId (${userId.censor()})" }
        val response = client.get<ApiTokenResponse>("${BASE_AUTH_URL}/users/$userId/apiToken") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received apiToken (${response.apiToken.censor()})" }
        return response
    }

    /**
     * Get all available networks, including groups and projects within the network
     */
    suspend fun networks(accessToken: String): NetworksResponse {
        logger.info { "Requesting networks" }
        val response = client.get<NetworksResponse>("${BASE_API_URL}/network") {
            headers.append(ACCESS_TOKEN_HEADER, accessToken)
        }
        logger.info { "Received a list networks with size (${response.size})" }
        return response
    }

    /**
     * Get available devices for the selected network/group/project
     */
    suspend fun backends(accessToken: String): BackendsResponse {
        logger.info { "Requesting backends" }
        val response = client.get<BackendsResponse>("${BASE_API_URL}/users/backends") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received a list of backends with size (${response.size})" }
        return response
    }

    /**
     * Get queued or running jobs
     */
    suspend fun jobs(accessToken: String): JobsResponse {
        logger.info { "Requesting jobs" }
        val response = client.get<JobsResponse>("${BASE_API_URL}/jobs/v2") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received a list of jobs with size (${response.items.size})" }
        return response
    }

    /**
     * Get the limit of jobs in the queue
     */
    suspend fun jobsLimit(
        accessToken: String,
        network: String,
        group: String,
        project: String,
        device: String
    ): JobsLimitResponse {
        logger.info { "Requesting jobs limit" }
        val response = client.get<JobsLimitResponse>(
            "${BASE_API_URL}/network/$network/groups/$group" +
                    "/projects/$project/devices/$device/jobsLimit"
        ) {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info {
            "Received a jobs limit of runningJobs (${response.runningJobs}) / maximumJobs (${response.maximumJobs})"
        }
        return response
    }

    /**
     * Run QASM code on a device
     */
    suspend fun runExperiment(
        accessToken: String,
        request: RunExperimentRequest,
        network: String,
        group: String,
        project: String
    ): RunExperimentResponse {
        logger.info {
            "Attempting to run an experiment with idCode (${request.codeId}) " +
                    "on backend with name (${request.backend.name})"
        }
        val response = client.post<RunExperimentResponse>(
            "${BASE_API_URL}/network/$network/groups/$group/" +
                    "projects/$project/runExperiment"
        ) {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
            contentType(ContentType.Application.Json)
            body = request
        }
        logger.info { "Received a job with id (${response.id})" }
        return response
    }

    /**
     * Upload the latest version of a saved code (circuit)
     */
    suspend fun versions(accessToken: String, request: VersionsRequest, code: String): VersionsResponse {
        logger.info { "Attempting to update a version with codeId (${request.idCode})" }
        val response = client.post<VersionsResponse>("${BASE_API_URL}/codes/$code/versions") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
            contentType(ContentType.Application.Json)
            body = request
        }
        logger.info { "Received updated version with codeId (${request.idCode})" }
        return response
    }

    /**
     * Get the latest version of a saved code
     */
    suspend fun latest(accessToken: String, codeId: String): LatestResponse {
        logger.info { "Requesting latest version of code ($codeId)" }
        val response = client.get<LatestResponse>("${BASE_API_URL}/codes/$codeId/versions/latest") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received version (${response.versionId}) of code ($codeId)" }
        return response
    }

    /**
     * Get all saved codes
     */
    suspend fun lastest(accessToken: String, userId: String): LastestResponse {
        logger.info { "Request lastest codes for userId (${userId.censor()})" }
        val response = client.get<LastestResponse>("${BASE_API_URL}/users/$userId/codes/lastest") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received codes with count (${response.count})" }
        return response
    }

    /**
     * Upload a new code
     */
    suspend fun newCode(accessToken: String, request: NewRequest): NewResponse {
        logger.info { "Attempting to create new code" }
        val response = client.post<NewResponse>("${BASE_API_URL}/codes") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
            contentType(ContentType.Application.Json)
            body = request
        }
        logger.info { "Received code with codeId (${response.idCode})" }
        return response
    }

    /**
     * Get the status of a specific job
     */
    suspend fun job(accessToken: String, jobId: String): JobResponse {
        logger.info { "Requesting the status of job ($jobId)" }
        val response = client.get<JobResponse>("${BASE_API_URL}/jobs/$jobId/v/1") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received job with status (${response.status})" }
        return response
    }

    /**
     * Get a URL to download the results of a job
     */
    suspend fun resultDownloadUrl(accessToken: String, jobId: String): ResultDownloadUrlResponse {
        logger.info { "Requesting result download URL for job ($jobId)" }
        val response = client.get<ResultDownloadUrlResponse>("${BASE_API_URL}/jobs/$jobId/resultDownloadUrl") {
            headers {
                append(ACCESS_TOKEN_HEADER, accessToken)
            }
        }
        logger.info { "Received result download URL" }
        return response
    }
}

expect class IbmqApi()

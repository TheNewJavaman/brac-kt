package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class JobsResponse(
    val meta: JobsMetaResponse,
    val items: List<JobsItemResponse>
)

@Serializable
data class JobsMetaResponse(
    val skip: Int,
    val limit: Int,
    val count: Int
)

@Serializable
data class JobsItemResponse(
    val id: String,
    val backend: JobsBackendResponse,
    val runMode: String? = null,
    val status: String,
    val creationDate: Instant,
    val name: String? = null,
    val tags: List<String>,
    val liveDataEnabled: Boolean,
    val provider: JobsProviderResponse,
    val userId: String,
    val endDate: Instant? = null,
    val createdBy: String,
    val infoQueue: JobsInfoQueueResponse
)

@Serializable
data class JobsBackendResponse(
    val name: String,
    val id: String
)

@Serializable
data class JobsProviderResponse(
    val hub: String,
    val group: String,
    val project: String,
    val backend: String
)

@Serializable
data class JobsInfoQueueResponse(
    val status: String,
    val position: Int,
    val hubPriority: Double,
    val groupPriority: Double,
    val projectPriority: Double,
    val estimatedStartTime: Instant,
    val estimatedCompleteTime: Instant
)

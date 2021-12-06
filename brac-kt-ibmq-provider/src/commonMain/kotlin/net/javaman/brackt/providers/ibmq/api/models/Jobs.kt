package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class JobsResponse(
    val meta: JobsMeta,
    val items: List<JobsItem>
)

@Serializable
data class JobsMeta(
    val skip: Int,
    val limit: Int,
    val count: Int
)

@Serializable
data class JobsItem(
    val id: String,
    val backend: Backend,
    val runMode: String? = null,
    val status: String,
    val creationDate: Instant,
    val name: String? = null,
    val tags: List<String>,
    val liveDataEnabled: Boolean,
    val provider: JobsItemProvider,
    val userId: String,
    val endDate: Instant? = null,
    val createdBy: String,
    val infoQueue: JobsItemInfoQueue? = null
)

@Serializable
data class JobsItemProvider(
    val hub: String,
    val group: String,
    val project: String,
    val backend: String
)

@Serializable
data class JobsItemInfoQueue(
    val status: String,
    val position: Int,
    val hubPriority: Double,
    val groupPriority: Double,
    val projectPriority: Double,
    val estimatedStartTime: Instant,
    val estimatedCompleteTime: Instant
)

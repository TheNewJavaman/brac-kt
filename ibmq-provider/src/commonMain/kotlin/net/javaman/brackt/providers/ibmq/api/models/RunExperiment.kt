package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class RunExperimentRequest(
    val qasms: List<ExperimentQasmRequest>,
    val backend: ExperimentBackendRequest,
    val codeId: String,
    @Required
    val shots: Int = 1024,
    @Required
    val name: String = "",
    @Required
    val tags: List<String> = emptyList()
)

@Serializable
data class ExperimentQasmRequest(
    val qasm: String
)

@Serializable
data class ExperimentBackendRequest(
    val name: String
)

@Serializable
data class RunExperimentResponse(
    val qasms: List<ExperimentQasmRequest>,
    val shots: Int? = null,
    val backend: ExperimentBackendResponse,
    val status: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val timePerStep: Map<String, Instant>,
    val ip: ExperimentIpResponse,
    val hubInfo: ExperimentHubInfoResponse,
    val liveDataEnabled: Boolean,
    val codeId: String,
    val code: String? = null,
    val name: String? = null,
    val tags: List<String>? = null,
    val id: String,
    val userId: String
)

@Serializable
data class ExperimentBackendResponse(
    val id: String,
    val name: String
)

@Serializable
data class ExperimentIpResponse(
    val ip: String,
    val city: String,
    val country: String,
    val continent: String
)

@Serializable
data class ExperimentHubInfoResponse(
    val hub: ExperimentNameResponse,
    val group: ExperimentNameResponse,
    val project: ExperimentNameResponse
)

@Serializable
data class ExperimentNameResponse(
    val name: String
)


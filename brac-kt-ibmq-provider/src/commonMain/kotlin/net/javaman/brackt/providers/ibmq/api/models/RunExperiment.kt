@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Serializable
data class RunExperimentRequest(
    val qasms: List<RunExperimentQasm>,
    val backend: RunExperimentBackend,
    val codeId: String,
    @Required
    val shots: Int = 1024,
    @Required
    val name: String = "",
    @Required
    val tags: List<String> = emptyList()
)

@JsExport
@Serializable
data class RunExperimentQasm(
    val qasm: String
)

@JsExport
@Serializable
data class RunExperimentBackend(
    val name: String
)

@JsExport
@Serializable
data class RunExperimentResponse(
    val qasms: List<RunExperimentQasm>,
    val shots: Int? = null,
    val backend: Backend,
    val status: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val timePerStep: Map<String, Instant>,
    val ip: RunExperimentIp,
    val hubInfo: HubInfo,
    val liveDataEnabled: Boolean,
    val codeId: String,
    val code: String? = null,
    val name: String? = null,
    val tags: List<String>? = null,
    val id: String,
    val userId: String
)

@JsExport
@Serializable
data class RunExperimentIp(
    val ip: String,
    val city: String,
    val country: String,
    val continent: String
)

package net.javaman.brackt.providers.ibmq.api.models

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

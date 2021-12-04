package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobResponse(
    val kind: String? = null,
    val backend: Backend,
    val status: String,
    val creationDate: Instant,
    val objectStorageInfo: Map<String, String>,
    val deleted: Boolean,
    val summaryData: JobSummaryData? = null,
    val timePerStep: Map<String, Instant>,
    val hubInfo: HubInfo,
    val codeId: String,
    val name: String,
    val tags: List<String>,
    val endDate: Instant? = null,
    val cost: Double? = null,
    val runMode: String? = null,
    val id: String,
    val userId: String,
    val liveDataEnabled: Boolean,
    val createdBy: String,
    val clientInfo: JobClientInfo
)

@Serializable
data class JobSummaryData(
    val size: JobSummaryDataSize,
    val success: Boolean? = null,
    val summary: JobSummaryDataSummary? = null,
    val resultTime: Double? = null
)

@Serializable
data class JobSummaryDataSize(
    val input: Int,
    val output: Int? = null
)

@Serializable
data class JobSummaryDataSummary(
    @SerialName("qobj_config")
    val qobjConfig: JobSummaryDataSummaryQobjConfig,
    @SerialName("gates_executed")
    val gatesExecuted: Int,
    @SerialName("partial_validation")
    val partialValidation: Boolean,
    @SerialName("num_circuits")
    val numCircuits: Int,
    @SerialName("max_qubits_used")
    val maxQubitsUsed: Int
)

@Serializable
data class JobSummaryDataSummaryQobjConfig(
    @SerialName("n_qubits")
    val nQubits: Int,
    val type: String,
    val shots: Int,
    @SerialName("memory_slots")
    val memorySlots: Int,
    val cost: Double
)

@Serializable
data class JobClientInfo(
    val name: String
)

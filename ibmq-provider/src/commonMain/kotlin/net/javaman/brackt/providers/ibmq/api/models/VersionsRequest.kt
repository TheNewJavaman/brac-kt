package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class VersionsRequest(
    @Required
    val description: Map<String, String> = mapOf("en" to ""),
    @Required
    val type: String = "Experiment",
    @Required
    val active: Boolean = true,
    val idCode: String,
    val name: String,
    val qasm: String,
    val codeType: QasmVersion,
    @Required
    val creationDate: Instant = Clock.System.now(),
    @Required
    val deleted: Boolean = false,
    @Required
    val orderDate: Long = Clock.System.now().toEpochMilliseconds(),
    @Required
    val userDeleted: Boolean = false,
    @Required
    val isPublic: Boolean = false,
    @Required
    val lastUpdateDate: Instant = Clock.System.now(),
    val userId: String
)

@Serializable
enum class QasmVersion {
    QASM2
}

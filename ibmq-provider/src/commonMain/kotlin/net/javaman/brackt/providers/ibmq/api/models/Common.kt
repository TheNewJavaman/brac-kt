package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class CodeModel(
    @Required
    val description: Map<String, String> = mapOf("en" to ""),
    @Required
    val type: String = "Experiment",
    @Required
    val active: Boolean = true,
    val versionId: Int? = null,
    val idCode: String? = null,
    @Required
    val name: String = "Untitled Circuit",
    val qasm: String,
    @Required
    val codeType: QasmVersion = QasmVersion.QASM2,
    val creationDate: Instant = Clock.System.now(),
    val deleted: Boolean = false,
    val orderDate: Long = Clock.System.now().toEpochMilliseconds(),
    val userDeleted: Boolean = false,
    @Required
    val isPublic: Boolean = false,
    val lastUpdateDate: Instant = Clock.System.now(),
    val id: String? = null,
    val userId: String
)

@Serializable
enum class QasmVersion {
    QASM2
}

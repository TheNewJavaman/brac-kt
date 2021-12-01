package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class VersionsResponse(
    val description: Map<String, String>,
    val type: String,
    val active: Boolean,
    val versionId: Int,
    val idCode: String,
    val name: String,
    val qasm: String,
    val codeType: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val orderDate: Long,
    val userDeleted: Boolean,
    val isPublic: Boolean,
    val lastUpdateDate: Instant,
    val id: String,
    val userId: String
)

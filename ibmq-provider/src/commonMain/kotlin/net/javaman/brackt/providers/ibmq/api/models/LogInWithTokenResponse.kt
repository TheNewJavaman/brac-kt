package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class LogInWithTokenResponse(
    val id: String,
    val ttl: Long,
    val created: Instant,
    val userId: String
)

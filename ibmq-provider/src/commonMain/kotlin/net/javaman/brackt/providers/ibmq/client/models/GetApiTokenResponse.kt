package net.javaman.brackt.providers.ibmq.client.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GetApiTokenResponse(
    val apiToken: String,
    val userId: String,
    val date: Instant,
    val isValidForDeletion: Boolean,
    val id: String
)

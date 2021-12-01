package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable

@Serializable
data class LogInWithTokenRequest(
    val apiToken: String
)

package net.javaman.brakt.providers.ibmq.client.models

import kotlinx.serialization.Serializable

@Serializable
data class LogInWithTokenRequest(
    val apiToken: String
)

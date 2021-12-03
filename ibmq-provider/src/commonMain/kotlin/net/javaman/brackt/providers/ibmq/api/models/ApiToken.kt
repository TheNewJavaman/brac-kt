@file:Suppress("MatchingDeclarationName") // Keep the naming convention of the package

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ApiTokenResponse(
    val apiToken: String,
    val userId: String,
    val date: Instant,
    val isValidForDeletion: Boolean,
    val id: String
)

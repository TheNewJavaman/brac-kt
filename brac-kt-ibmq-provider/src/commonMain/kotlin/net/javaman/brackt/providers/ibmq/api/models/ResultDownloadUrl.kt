@file:Suppress("MatchingDeclarationName") // Keep the naming convention of the package

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultDownloadUrlResponse(
    val url: String
)

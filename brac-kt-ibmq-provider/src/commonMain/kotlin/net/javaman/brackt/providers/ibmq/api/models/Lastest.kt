@file:Suppress("MatchingDeclarationName") // Keep the naming convention of the package

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable

@Suppress("MatchingDeclarationName") // Keep the naming convention of the package
@Serializable
data class LastestResponse(
    val total: Int,
    val count: Int,
    val codes: List<Code>
)

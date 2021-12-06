@file:Suppress("MatchingDeclarationName") // Keep the naming convention of the package

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable

@Suppress("MatchingDeclarationName") // Keep the naming convention of the package
@Serializable
data class JobsLimitResponse(
    val maximumJobs: Int,
    val runningJobs: Int
)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable

@Serializable
data class JobsLimitResponse(
    val maximumJobs: Int,
    val runningJobs: Int
)
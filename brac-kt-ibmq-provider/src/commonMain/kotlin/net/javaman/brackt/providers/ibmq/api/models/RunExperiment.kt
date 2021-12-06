package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import net.javaman.brackt.api.util.formatters.decodedUrl
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.IbmqProvider
import net.javaman.brackt.providers.ibmq.JobTimeoutException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Serializable
data class RunExperimentRequest(
    val qasms: List<RunExperimentQasm>,
    val backend: RunExperimentBackend,
    val codeId: String,
    @Required
    val shots: Int = 1024,
    @Required
    val name: String = "",
    @Required
    val tags: List<String> = emptyList()
)

@Serializable
data class RunExperimentQasm(
    val qasm: String
)

@Serializable
data class RunExperimentBackend(
    val name: String
)

@Serializable
data class RunExperimentResponse(
    val qasms: List<RunExperimentQasm>,
    val shots: Int? = null,
    val backend: Backend,
    val status: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val timePerStep: Map<String, Instant>,
    val ip: RunExperimentIp,
    val hubInfo: HubInfo,
    val liveDataEnabled: Boolean,
    val codeId: String,
    val code: String? = null,
    val name: String? = null,
    val tags: List<String>? = null,
    val id: String,
    val userId: String
)

@Serializable
data class RunExperimentIp(
    val ip: String,
    val city: String,
    val country: String,
    val continent: String
)

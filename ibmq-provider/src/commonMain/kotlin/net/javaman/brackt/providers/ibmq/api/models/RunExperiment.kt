package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import net.javaman.brackt.api.util.concurrency.runSync
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
) {
    private val logger: Logger by injection()
    private val ibmqProvider: IbmqProvider by injection()

    /**
     * Run an experiment and wait for its completion
     */
    @OptIn(ExperimentalTime::class)
    fun andWait(timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_MINUTES.minutes): ResultResponse {
        logger.info { "Running an experiment and waiting for its completion" }
        val jobId = this.id
        val tStart = Clock.System.now()
        var runResponse = ibmqProvider.getRun(jobId)
        var tEnd = Clock.System.now()
        var tLastLog = Clock.System.now()
        while (runResponse.status != IbmqProvider.JOB_STATUS_COMPLETE) {
            runSync { delay(IbmqProvider.JOB_POLL_RATE_MS) }
            runResponse = ibmqProvider.getRun(jobId)
            tEnd = Clock.System.now()
            if (tEnd.minus(tLastLog) > IbmqProvider.JOB_LOG_RATE_MS.milliseconds) {
                logger.info { "Time elapsed since job was requested: (${tEnd.minus(tStart).inWholeSeconds}) seconds" }
                tLastLog = tEnd
            }
            if (tEnd.minus(tStart) > timeoutDuration) {
                throw JobTimeoutException(
                    "Job timed out after duration (${tEnd.minus(tStart).inWholeSeconds}) seconds " +
                            "seconds; the job is probably still active, but we're impatient!"
                )
            }
        }
        logger.info {
            "Total time elapsed from job request to response: (${tEnd.minus(tStart).inWholeSeconds}) seconds"
        }
        val resultDownloadUrl = ibmqProvider.getResultDownloadUrl(jobId).url.decodedUrl()
        val result = ibmqProvider.getResult(resultDownloadUrl)
        logger.info { result.results[0].data.pretty() }
        return result
    }
}

@Serializable
data class RunExperimentIp(
    val ip: String,
    val city: String,
    val country: String,
    val continent: String
)

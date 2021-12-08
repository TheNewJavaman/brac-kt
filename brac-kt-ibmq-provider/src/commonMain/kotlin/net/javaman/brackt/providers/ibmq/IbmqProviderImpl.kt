@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq

import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.formatters.decodedUrl
import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.api.IbmqApi
import net.javaman.brackt.providers.ibmq.api.IbmqApiImpl
import net.javaman.brackt.providers.ibmq.api.client
import net.javaman.brackt.providers.ibmq.api.models.BackendResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.Code
import net.javaman.brackt.providers.ibmq.api.models.LatestResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkGroup
import net.javaman.brackt.providers.ibmq.api.models.NetworkHub
import net.javaman.brackt.providers.ibmq.api.models.NetworkProject
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.NewResponse
import net.javaman.brackt.providers.ibmq.api.models.ResultResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentBackend
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentQasm
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse
import net.javaman.brackt.providers.ibmq.transpiler.toQasm
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmStatic
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

/**
 * An interface for [IbmqApi] that saves session data
 */
@Suppress("TooManyFunctions") // All are needed
open class IbmqProviderImpl {
    private val ibmqApi: IbmqApiImpl by injection()
    private val logger: Logger by injection()

    lateinit var accessToken: String
    lateinit var userId: String
    lateinit var network: NetworkHub
    lateinit var group: NetworkGroup
    lateinit var project: NetworkProject
    lateinit var device: BackendResponse
    lateinit var code: Code
    lateinit var jobId: String

    companion object {
        private const val DEVICE_STATUS_ACTIVE = "active"
        private const val JOB_STATUS_COMPLETE = "COMPLETED"
        private const val JOB_STATUS_CANCELLED = "CANCELLED"

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        val JOB_TIMEOUT_DURATION = 5.minutes

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        val JOB_POLL_INTERVAL = 2.seconds

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        private val JOB_LOG_INTERVAL = 10.seconds

        /**
         * Add injections for this module
         */
        @JvmStatic
        fun addInjections() = InjectionManager.add {
            one { IbmqApiImpl() }
            one { IbmqApi() }
            one { IbmqProviderImpl() }
            one { IbmqProvider() }
        }
    }

    /**
     * Log in using an API token; for JVM, pass an environment variable from [PropertyManager]
     */
    suspend fun logIn(apiToken: String): LogInWithTokenResponse {
        val request = LogInWithTokenRequest(apiToken)
        val response = ibmqApi.logInWithToken(request)
        accessToken = response.id
        userId = response.userId
        return response
    }

    /**
     * Get the API token associated with this session
     */
    suspend fun getApiToken() = ibmqApi.apiToken(accessToken, userId)

    /**
     * Select the default network, including the default group and project
     */
    suspend fun selectNetwork() = selectNetwork { networks ->
        network = networks.first { it.isDefault }
        val groupEntry = network.groups.entries.first { it.value.isDefault }
        group = groupEntry.value
        val projectEntry = groupEntry.value.projects.entries.first { it.value.isDefault }
        project = projectEntry.value
    }

    /**
     * Select a network manually using a lambda
     */
    @JsName("selectNetworkWithPicker")
    suspend fun selectNetwork(picker: (NetworksResponse) -> Unit) {
        val response = ibmqApi.networks(accessToken)
        picker(response)
        logger.info { "Selected network with name (${network.name})" }
        logger.info { "Selected group with name (${group.name})" }
        logger.info { "Selected project with name (${project.name})" }
    }

    /**
     * Select a device; default choice predicates:
     * - Simulator
     * - 5 or more qubits
     * - Smallest queue
     */
    suspend fun selectDevice(simulator: Boolean = true, minQubits: Int = 5) = selectDevice { devices ->
        val projectDeviceNames = project.devices.values.map { it.name }
        device = devices.filter {
            projectDeviceNames.contains(it.name) &&
                    it.isSimulator == simulator &&
                    it.qubits >= minQubits &&
                    it.deviceStatus.state &&
                    it.deviceStatus.status == DEVICE_STATUS_ACTIVE
        }.minByOrNull {
            it.queueLength
        } ?: throw DeviceNotAvailableException(
            "Could not select a device because none matched the default predicates"
        )
    }

    /**
     * Select a device using a custom lambda
     */
    @JsName("selectDeviceWithPicker")
    suspend fun selectDevice(picker: (BackendsResponse) -> Unit) {
        val response = ibmqApi.backends(accessToken)
        picker(response)
        logger.info { "Selected device with name (${device.name})" }
    }

    /**
     * Get the currently running jobs
     */
    suspend fun getJobs() = ibmqApi.jobs(accessToken)

    /**
     * Get the number of currently running and maximum number of jobs
     */
    suspend fun getJobsLimit() =
        ibmqApi.jobsLimit(accessToken, network.name, group.name, project.name, device.name)

    /**
     * Run an experiment on a device
     */
    @JsName("runExperimentWithRequest")
    suspend fun runExperiment(request: RunExperimentRequest): RunExperimentResponse {
        val response = ibmqApi.runExperiment(
            accessToken,
            request,
            network.name,
            group.name,
            project.name
        )
        jobId = response.id
        return response
    }

    /**
     * Run a [QuantumCircuit] on a device
     */
    suspend fun runExperiment(qc: QuantumCircuit, shots: Int = 1024, name: String = ""): RunExperimentResponse {
        return runExperiment(
            RunExperimentRequest(
                qasms = listOf(
                    RunExperimentQasm(
                        qasm = qc.toQasm()
                    )
                ),
                backend = RunExperimentBackend(
                    name = device.name
                ),
                codeId = "",
                shots = shots,
                name = name
            )
        )
    }

    /**
     * Upload a new version to a saved circuit
     */
    suspend fun updateExperiment(codeId: String, request: VersionsRequest): VersionsResponse {
        val response = ibmqApi.versions(accessToken, request, codeId)
        code = response
        return response
    }

    /**
     * Get the latest version of a circuit
     */
    suspend fun getExperiment(codeId: String): LatestResponse {
        val response = ibmqApi.latest(accessToken, codeId)
        code = response
        return response
    }

    /**
     * Get all saved circuits
     */
    suspend fun getExperiments() = ibmqApi.lastest(accessToken, userId)

    /**
     * Save a new circuit
     */
    suspend fun newExperiment(request: NewRequest): NewResponse {
        val response = ibmqApi.newCode(accessToken, request)
        code = response
        return response
    }

    /**
     * Get the status of a run
     */
    suspend fun getRun(jobId: String) = ibmqApi.job(accessToken, jobId)

    /**
     * Get a URL for the results of a run
     */
    suspend fun getResultDownloadUrl(jobId: String) = ibmqApi.resultDownloadUrl(accessToken, jobId)

    /**
     * Get the results of a run from a URL
     */
    suspend fun getResult(url: String): ResultResponse {
        logger.info { "Getting results from a URL" }
        val response = client.get<ResultResponse>(url)
        logger.info { "Received results download with size (${response.results.size})" }
        return response
    }

    @OptIn(ExperimentalTime::class)
    suspend fun andWait(timeoutDuration: Duration = JOB_TIMEOUT_DURATION): ResultResponse {
        val tStart = Clock.System.now()
        var runResponse = getRun(jobId)
        var tEnd = Clock.System.now()
        var tLastLog = Clock.System.now()
        while (runResponse.status != JOB_STATUS_COMPLETE && runResponse.status != JOB_STATUS_CANCELLED) {
            delay(JOB_POLL_INTERVAL)
            runResponse = getRun(jobId)
            tEnd = Clock.System.now()
            if (tEnd.minus(tLastLog) > JOB_LOG_INTERVAL) {
                logger.info { "Time elapsed since job was requested: (${tEnd.minus(tStart).inWholeSeconds}) seconds" }
                tLastLog = tEnd
            }
            if (tEnd.minus(tStart) > timeoutDuration) {
                throw JobTimeoutException(
                    "Job timed out after duration (${tEnd.minus(tStart).inWholeSeconds}) seconds; " +
                            "the job is probably still active, but we're impatient!"
                )
            }
        }
        if (runResponse.status == JOB_STATUS_CANCELLED) throw JobCancelledException("Job was cancelled")
        logger.info {
            "Total time elapsed from job request to response: (${tEnd.minus(tStart).inWholeSeconds}) seconds"
        }
        val resultDownloadUrl = getResultDownloadUrl(jobId).url.decodedUrl()
        val result = getResult(resultDownloadUrl)
        logger.info { result.results[0].data.pretty() }
        return result
    }

    /**
     * Run an experiment and wait for the job to complete
     */
    @OptIn(ExperimentalTime::class)
    @JsName("runExperimentAndWaitWithRequest")
    suspend fun runExperimentAndWait(
        request: RunExperimentRequest,
        timeoutDuration: Duration = JOB_TIMEOUT_DURATION
    ): ResultResponse {
        runExperiment(request)
        return andWait(timeoutDuration)
    }

    /**
     * Run an experiment and wait for the job to complete
     */
    @OptIn(ExperimentalTime::class)
    suspend fun runExperimentAndWait(
        qc: QuantumCircuit,
        timeoutDuration: Duration = JOB_TIMEOUT_DURATION
    ): ResultResponse {
        runExperiment(qc)
        return andWait(timeoutDuration)
    }
}

@Suppress("UtilityClassWithPublicConstructor") // It's used in the `actual` implementations
expect class IbmqProvider()

@JsExport
class DeviceNotAvailableException(message: String) : Exception(message)

@JsExport
class JobTimeoutException(message: String) : Exception(message)

@JsExport
class JobCancelledException(message: String) : Exception(message)

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
import net.javaman.brackt.providers.ibmq.api.apiToken
import net.javaman.brackt.providers.ibmq.api.backends
import net.javaman.brackt.providers.ibmq.api.client
import net.javaman.brackt.providers.ibmq.api.job
import net.javaman.brackt.providers.ibmq.api.jobs
import net.javaman.brackt.providers.ibmq.api.jobsLimit
import net.javaman.brackt.providers.ibmq.api.lastest
import net.javaman.brackt.providers.ibmq.api.latest
import net.javaman.brackt.providers.ibmq.api.logInWithToken
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
import net.javaman.brackt.providers.ibmq.api.networks
import net.javaman.brackt.providers.ibmq.api.newCode
import net.javaman.brackt.providers.ibmq.api.resultDownloadUrl
import net.javaman.brackt.providers.ibmq.api.runExperiment
import net.javaman.brackt.providers.ibmq.api.versions
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
@JsExport
class IbmqProvider {
    val ibmqApi: IbmqApi by injection()
    val logger: Logger by injection()

    lateinit var accessToken: String
    lateinit var userId: String
    lateinit var network: NetworkHub
    lateinit var group: NetworkGroup
    lateinit var project: NetworkProject
    lateinit var device: BackendResponse
    lateinit var code: Code
    lateinit var jobId: String

    companion object {
        const val DEVICE_STATUS_ACTIVE = "active"
        const val JOB_STATUS_COMPLETE = "COMPLETED"

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        val JOB_TIMEOUT_DURATION = 5.minutes

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        val JOB_POLL_INTERVAL = 2.seconds

        @OptIn(ExperimentalTime::class)
        @JvmStatic
        val JOB_LOG_INTERVAL = 10.seconds

        /**
         * Add injections for this module
         */
        @JvmStatic
        fun addInjections() = InjectionManager.add {
            one { IbmqApi() }
            one { IbmqProvider() }
        }
    }
}

/**
 * Log in using an API token; for JVM, pass an environment variable from [PropertyManager]
 */
suspend fun IbmqProvider.logIn(apiToken: String): LogInWithTokenResponse {
    val request = LogInWithTokenRequest(apiToken)
    val response = ibmqApi.logInWithToken(request)
    accessToken = response.id
    userId = response.userId
    return response
}

/**
 * Get the API token associated with this session
 */
suspend fun IbmqProvider.getApiToken() = ibmqApi.apiToken(accessToken, userId)

/**
 * Select the default network, including the default group and project
 */
suspend fun IbmqProvider.selectNetwork() = selectNetwork { networks ->
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
suspend fun IbmqProvider.selectNetwork(picker: IbmqProvider.(NetworksResponse) -> Unit) {
    val response = ibmqApi.networks(accessToken)
    picker(this, response)
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
suspend fun IbmqProvider.selectDevice(simulator: Boolean = true, minQubits: Int = 5) = selectDevice { devices ->
    val projectDeviceNames = project.devices.values.map { it.name }
    device = devices.filter {
        projectDeviceNames.contains(it.name) &&
                it.isSimulator == simulator &&
                it.qubits >= minQubits &&
                it.deviceStatus.state &&
                it.deviceStatus.status == IbmqProvider.DEVICE_STATUS_ACTIVE
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
suspend fun IbmqProvider.selectDevice(picker: IbmqProvider.(BackendsResponse) -> Unit) {
    val response = ibmqApi.backends(accessToken)
    picker(this, response)
    logger.info { "Selected device with name (${device.name})" }
}

/**
 * Get the currently running jobs
 */
suspend fun IbmqProvider.getJobs() = ibmqApi.jobs(accessToken)

/**
 * Get the number of currently running and maximum number of jobs
 */
suspend fun IbmqProvider.getJobsLimit() =
    ibmqApi.jobsLimit(accessToken, network.name, group.name, project.name, device.name)

/**
 * Run an experiment on a device
 */
@JsName("runExperimentWithRequest")
suspend fun IbmqProvider.runExperiment(request: RunExperimentRequest): RunExperimentResponse {
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
suspend fun IbmqProvider.runExperiment(qc: QuantumCircuit, shots: Int = 1024, name: String = "") = runExperiment(
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

/**
 * Upload a new version to a saved circuit
 */
suspend fun IbmqProvider.updateExperiment(codeId: String, request: VersionsRequest): VersionsResponse {
    val response = ibmqApi.versions(accessToken, request, codeId)
    code = response
    return response
}

/**
 * Get the latest version of a circuit
 */
suspend fun IbmqProvider.getExperiment(codeId: String): LatestResponse {
    val response = ibmqApi.latest(accessToken, codeId)
    code = response
    return response
}

/**
 * Get all saved circuits
 */
suspend fun IbmqProvider.getExperiments() = ibmqApi.lastest(accessToken, userId)

/**
 * Save a new circuit
 */
suspend fun IbmqProvider.newExperiment(request: NewRequest): NewResponse {
    val response = ibmqApi.newCode(accessToken, request)
    code = response
    return response
}

/**
 * Get the status of a run
 */
suspend fun IbmqProvider.getRun(jobId: String) = ibmqApi.job(accessToken, jobId)

/**
 * Get a URL for the results of a run
 */
suspend fun IbmqProvider.getResultDownloadUrl(jobId: String) = ibmqApi.resultDownloadUrl(accessToken, jobId)

/**
 * Get the results of a run from a URL
 */
suspend fun IbmqProvider.getResult(url: String): ResultResponse {
    logger.info { "Getting results from a URL" }
    val response = client.get<ResultResponse>(url)
    logger.info { "Received results download with size (${response.results.size})" }
    return response
}

@OptIn(ExperimentalTime::class)
suspend fun IbmqProvider.andWait(timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION): ResultResponse {
    val tStart = Clock.System.now()
    var runResponse = getRun(jobId)
    var tEnd = Clock.System.now()
    var tLastLog = Clock.System.now()
    while (runResponse.status != IbmqProvider.JOB_STATUS_COMPLETE) {
        delay(IbmqProvider.JOB_POLL_INTERVAL)
        runResponse = getRun(jobId)
        tEnd = Clock.System.now()
        if (tEnd.minus(tLastLog) > IbmqProvider.JOB_LOG_INTERVAL) {
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
suspend fun IbmqProvider.runExperimentAndWait(
    request: RunExperimentRequest,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
): ResultResponse {
    runExperiment(request)
    return andWait(timeoutDuration)
}

/**
 * Run an experiment and wait for the job to complete
 */
@OptIn(ExperimentalTime::class)
suspend fun IbmqProvider.runExperimentAndWait(
    qc: QuantumCircuit,
    timeoutDuration: Duration = IbmqProvider.JOB_TIMEOUT_DURATION
): ResultResponse {
    runExperiment(qc)
    return andWait(timeoutDuration)
}

@JsExport
class DeviceNotAvailableException(message: String) : Exception(message)

@JsExport
class JobTimeoutException(message: String) : Exception(message)

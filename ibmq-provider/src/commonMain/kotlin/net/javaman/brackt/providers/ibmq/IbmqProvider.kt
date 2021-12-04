package net.javaman.brackt.providers.ibmq

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import net.javaman.brackt.api.quantum.QuantumCircuit
import net.javaman.brackt.api.util.concurrency.runSync
import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.api.IbmqApi
import net.javaman.brackt.providers.ibmq.api.models.ApiTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.Code
import net.javaman.brackt.providers.ibmq.api.models.JobResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsLimitResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsResponse
import net.javaman.brackt.providers.ibmq.api.models.LastestResponse
import net.javaman.brackt.providers.ibmq.api.models.LatestResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkGroup
import net.javaman.brackt.providers.ibmq.api.models.NetworkHub
import net.javaman.brackt.providers.ibmq.api.models.NetworkProject
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.NewResponse
import net.javaman.brackt.providers.ibmq.api.models.ResultDownloadUrlResponse
import net.javaman.brackt.providers.ibmq.api.models.ResultResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentBackend
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentQasm
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse
import net.javaman.brackt.providers.ibmq.transpiler.toQasm
import kotlin.jvm.JvmStatic

/**
 * An interface for [IbmqApi] that saves session data
 */
@Suppress("TooManyFunctions") // All are needed
class IbmqProvider {
    private val ibmqApi: IbmqApi by injection()
    private val logger: Logger by injection()

    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

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
        const val JOB_TIMEOUT_MINUTES = 10
        const val JOB_POLL_RATE_MS = 2_000L
        const val JOB_LOG_RATE_MS = 10_000

        /**
         * Add injections for this module
         */
        @JvmStatic
        fun addInjections() = InjectionManager.add {
            one { IbmqApi() }
            one { IbmqProvider() }
        }
    }

    /**
     * Log in using an API token; for JVM, pass an environment variable from [PropertyManager]
     */
    fun logIn(apiToken: String): LogInWithTokenResponse {
        logger.info { "Logging in with an API token" }
        val request = LogInWithTokenRequest(apiToken)
        val response = runSync { ibmqApi.logInWithToken(request) }
        accessToken = response.id
        userId = response.userId
        return response
    }

    /**
     * Get the API token associated with this session
     */
    fun getApiToken(): ApiTokenResponse {
        logger.info { "Getting API token" }
        return runSync { ibmqApi.apiToken(accessToken, userId) }
    }

    /**
     * Select the default network, including the default group and project
     */
    fun selectNetwork() {
        selectNetwork { networks ->
            network = networks.first { it.isDefault }
            val groupEntry = network.groups.entries.first { it.value.isDefault }
            group = groupEntry.value
            val projectEntry = groupEntry.value.projects.entries.first { it.value.isDefault }
            project = projectEntry.value
        }
    }

    /**
     * Select a network manually using a lambda
     */
    fun selectNetwork(picker: IbmqProvider.(NetworksResponse) -> Unit) {
        logger.info { "Selecting network, group, and project" }
        val response = runSync { ibmqApi.networks(accessToken) }
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
    fun selectDevice(simulator: Boolean = true, minQubits: Int = 5) {
        selectDevice { devices ->
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
    }

    /**
     * Select a device using a custom lambda
     */
    fun selectDevice(picker: IbmqProvider.(BackendsResponse) -> Unit) {
        logger.info { "Selecting device" }
        val response = runSync { ibmqApi.backends(accessToken) }
        picker(this, response)
        logger.info { "Selected device with name (${device.name})" }
    }

    /**
     * Get the currently running jobs
     */
    fun getJobs(): JobsResponse {
        logger.info { "Getting jobs" }
        return runSync { ibmqApi.jobs(accessToken) }
    }

    /**
     * Get the number of currently running and maximum number of jobs
     */
    fun getJobsLimit(): JobsLimitResponse {
        logger.info { "Getting jobs limit" }
        return runSync { ibmqApi.jobsLimit(accessToken, network.name, group.name, project.name, device.name) }
    }

    /**
     * Run an experiment on a device
     */
    fun runExperiment(request: RunExperimentRequest): RunExperimentResponse {
        logger.info { "Running an experiment" }
        val response = runSync {
            ibmqApi.runExperiment(
                accessToken,
                request,
                network.name,
                group.name,
                project.name
            )
        }
        jobId = response.id
        return response
    }

    /**
     * Run a [QuantumCircuit] on a device
     */
    fun runExperiment(qc: QuantumCircuit, shots: Int = 1024, name: String = "") = runExperiment(
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
    fun updateExperiment(codeId: String, request: VersionsRequest): VersionsResponse {
        logger.info { "Updating an experiment's version" }
        val response = runSync { ibmqApi.versions(accessToken, request, codeId) }
        code = response
        return response
    }

    /**
     * Get the latest version of a circuit
     */
    fun getExperiment(codeId: String): LatestResponse {
        logger.info { "Getting latest version of an experiment" }
        val response = runSync { ibmqApi.latest(accessToken, codeId) }
        code = response
        return response
    }

    /**
     * Get all saved circuits
     */
    fun getExperiments(): LastestResponse {
        logger.info { "Getting experiments" }
        return runSync { ibmqApi.lastest(accessToken, userId) }
    }

    /**
     * Save a new circuit
     */
    fun newExperiment(request: NewRequest): NewResponse {
        logger.info { "Saving an experiment" }
        val response = runSync { ibmqApi.newCode(accessToken, request) }
        code = response
        return response
    }

    /**
     * Get the status of a run
     */
    fun getRun(jobId: String): JobResponse {
        logger.info { "Getting the status of a job" }
        return runSync { ibmqApi.job(accessToken, jobId) }
    }

    /**
     * Get a URL for the results of a run
     */
    fun getResultDownloadUrl(jobId: String): ResultDownloadUrlResponse {
        logger.info { "Getting result download URL" }
        return runSync { ibmqApi.resultDownloadUrl(accessToken, jobId) }
    }

    /**
     * Get the results of a run from a URL
     */
    fun getResult(url: String): ResultResponse {
        logger.info { "Getting results from a URL" }
        val response = runSync { client.get<ResultResponse>(url) }
        logger.info { "Received results download with size (${response.results.size})" }
        return response
    }
}

class DeviceNotAvailableException(message: String) : Exception(message)

class JobTimeoutException(message: String) : Exception(message)

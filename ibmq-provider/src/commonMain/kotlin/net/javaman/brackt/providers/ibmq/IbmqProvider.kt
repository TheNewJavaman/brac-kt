package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.concurrency.runSync
import net.javaman.brackt.api.util.formatters.censor
import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.api.IbmqApi
import net.javaman.brackt.providers.ibmq.api.models.ApiTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.CodeModel
import net.javaman.brackt.providers.ibmq.api.models.JobsLimitResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsResponse
import net.javaman.brackt.providers.ibmq.api.models.LastestResponse
import net.javaman.brackt.providers.ibmq.api.models.LatestResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkGroupResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkProjectResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.NewRequest
import net.javaman.brackt.providers.ibmq.api.models.NewResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse
import kotlin.jvm.JvmStatic

/**
 * An interface for [IbmqApi] that saves session data
 */
@Suppress("TooManyFunctions") // All are needed
class IbmqProvider {
    private val ibmqApi: IbmqApi by injection()
    private val logger: Logger by injection()

    lateinit var accessToken: String
    lateinit var userId: String
    lateinit var network: NetworkResponse
    lateinit var group: NetworkGroupResponse
    lateinit var project: NetworkProjectResponse
    lateinit var device: BackendResponse
    lateinit var code: CodeModel

    companion object {
        const val DEVICE_STATUS_ACTIVE = "active"

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
        logger.info { "Logging in with apiToken (${apiToken.censor()})" }
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
        return runSync {
            ibmqApi.runExperiment(
                accessToken,
                request,
                network.name,
                group.name,
                project.name
            )
        }
    }

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
}

class DeviceNotAvailableException(message: String) : Exception(message)

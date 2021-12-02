package net.javaman.brackt.providers.ibmq

import net.javaman.brackt.api.util.formatters.censor
import net.javaman.brackt.api.util.injections.InjectionManager
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.api.IbmqApi
import net.javaman.brackt.providers.ibmq.api.apiTokenSync
import net.javaman.brackt.providers.ibmq.api.backendsSync
import net.javaman.brackt.providers.ibmq.api.jobsLimitSync
import net.javaman.brackt.providers.ibmq.api.jobsSync
import net.javaman.brackt.providers.ibmq.api.logInWithTokenSync
import net.javaman.brackt.providers.ibmq.api.models.ApiTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendResponse
import net.javaman.brackt.providers.ibmq.api.models.BackendsResponse
import net.javaman.brackt.providers.ibmq.api.models.ExperimentBackendRequest
import net.javaman.brackt.providers.ibmq.api.models.ExperimentQasmRequest
import net.javaman.brackt.providers.ibmq.api.models.JobsLimitResponse
import net.javaman.brackt.providers.ibmq.api.models.JobsResponse
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenRequest
import net.javaman.brackt.providers.ibmq.api.models.LogInWithTokenResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkGroupResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkProjectResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworkResponse
import net.javaman.brackt.providers.ibmq.api.models.NetworksResponse
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentRequest
import net.javaman.brackt.providers.ibmq.api.models.RunExperimentResponse
import net.javaman.brackt.providers.ibmq.api.models.VersionsRequest
import net.javaman.brackt.providers.ibmq.api.models.VersionsResponse
import net.javaman.brackt.providers.ibmq.api.networksSync
import net.javaman.brackt.providers.ibmq.api.runExperimentSync
import net.javaman.brackt.providers.ibmq.api.versionsSync
import net.javaman.brackt.providers.ibmq.transpiler.QasmProgram
import kotlin.jvm.JvmStatic

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

    companion object {
        const val DEVICE_STATUS_ACTIVE = "active"
        const val DEFAULT_DESCRIPTION_LANGUAGE = "en"

        @JvmStatic
        fun addInjections() = InjectionManager.add {
            one { IbmqApi() }
            one { IbmqProvider() }
        }
    }

    fun logIn(apiToken: String): LogInWithTokenResponse {
        logger.info { "Logging in with apiToken (${apiToken.censor()})" }
        val request = LogInWithTokenRequest(apiToken)
        val response = ibmqApi.logInWithTokenSync(request)
        accessToken = response.id
        userId = response.userId
        return response
    }

    fun getApiToken(): ApiTokenResponse {
        logger.info { "Getting API token" }
        return ibmqApi.apiTokenSync(accessToken, userId)
    }

    fun selectNetwork() {
        selectNetwork { networks ->
            network = networks.first { it.isDefault }
            val groupEntry = network.groups.entries.first { it.value.isDefault }
            group = groupEntry.value
            val projectEntry = groupEntry.value.projects.entries.first { it.value.isDefault }
            project = projectEntry.value
        }
    }

    fun selectNetwork(picker: IbmqProvider.(NetworksResponse) -> Unit) {
        logger.info { "Selecting network, group, and project" }
        val response = ibmqApi.networksSync(accessToken)
        picker(this, response)
        logger.info { "Selected network with name (${network.name})" }
        logger.info { "Selected group with name (${group.name})" }
        logger.info { "Selected project with name (${project.name})" }
    }

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

    fun selectDevice(picker: IbmqProvider.(BackendsResponse) -> Unit) {
        logger.info { "Selecting device" }
        val response = ibmqApi.backendsSync(accessToken)
        picker(this, response)
        logger.info { "Selected device with name (${device.name})" }
    }

    fun getJobs(): JobsResponse {
        logger.info { "Getting jobs" }
        return ibmqApi.jobsSync(accessToken)
    }

    fun getJobsLimit(): JobsLimitResponse {
        logger.info { "Getting jobs limit" }
        return ibmqApi.jobsLimitSync(accessToken, network.name, group.name, project.name, device.name)
    }

    fun runExperiment(request: RunExperimentRequest): RunExperimentResponse {
        logger.info { "Running an experiment" }
        return ibmqApi.runExperimentSync(
            accessToken,
            request,
            network.name,
            group.name,
            project.name
        )
    }

    fun runExperiment(
        program: QasmProgram,
        shots: Int = 1024,
        tags: List<String> = emptyList()
    ): RunExperimentResponse {
        logger.info { "Running an experiment" }
        return ibmqApi.runExperimentSync(
            accessToken,
            RunExperimentRequest(
                qasms = listOf(ExperimentQasmRequest(qasm = program.qasm)),
                backend = ExperimentBackendRequest(name = device.name),
                codeId = program.idCode,
                shots = shots,
                name = program.name,
                tags = tags
            ),
            network.name,
            group.name,
            project.name
        )
    }

    fun updateExperiment(codeId: String, request: VersionsRequest): VersionsResponse {
        logger.info { "Updating an experiment's version" }
        return ibmqApi.versionsSync(accessToken, request, codeId)
    }

    fun updateExperiment(codeId: String, program: QasmProgram): VersionsResponse {
        logger.info { "Updating an experiment's version" }
        return ibmqApi.versionsSync(
            accessToken,
            VersionsRequest(
                description = mapOf(DEFAULT_DESCRIPTION_LANGUAGE to program.description),
                idCode = program.idCode,
                name = program.name,
                qasm = program.qasm,
                codeType = program.version,
                creationDate = program.creationDate,
                lastUpdateDate = program.lastUpdateDate,
                userId = userId
            ),
            codeId
        )
    }
}

class DeviceNotAvailableException(message: String) : Exception(message)

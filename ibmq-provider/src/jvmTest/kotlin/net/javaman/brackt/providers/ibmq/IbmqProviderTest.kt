package net.javaman.brackt.providers.ibmq

import io.ktor.client.features.ClientRequestException
import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.providers.ibmq.TestData.API_TOKEN
import net.javaman.brackt.providers.ibmq.TestData.CODE_ID
import net.javaman.brackt.providers.ibmq.TestData.QASM_PROGRAM
import net.javaman.brackt.providers.ibmq.TestData.RUN_EXPERIMENT_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.VERSIONS_REQUEST
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IbmqProviderTest {
    private val ibmqProvider: IbmqProvider by injection()
    private val logger: Logger by injection()

    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    @Test
    @Order(1)
    fun logIn_ok() {
        ibmqProvider.logIn(API_TOKEN)
    }

    @Test
    @Order(2)
    fun getApiToken_ok() {
        ibmqProvider.getApiToken()
    }

    @Test
    @Order(3)
    fun selectNetwork_default_ok() {
        ibmqProvider.selectNetwork()
    }

    @Test
    @Order(4)
    fun selectNetwork_custom_ok() {
        ibmqProvider.selectNetwork { networks ->
            network = networks.first { it.isDefault }
            val groupEntry = network.groups.entries.first { it.value.isDefault }
            group = groupEntry.value
            val projectEntry = groupEntry.value.projects.entries.first { it.value.isDefault }
            project = projectEntry.value
        }
    }

    @Test
    @Order(5)
    fun selectDevice_default_ok() {
        ibmqProvider.selectDevice()
    }

    @Test
    @Order(6)
    fun selectDevice_custom_ok() {
        ibmqProvider.selectDevice { devices ->
            val projectDeviceNames = project.devices.values.map { it.name }
            device = devices.filter {
                projectDeviceNames.contains(it.name) &&
                        it.isSimulator &&
                        it.qubits >= 5 &&
                        it.deviceStatus.state &&
                        it.deviceStatus.status == IbmqProvider.DEVICE_STATUS_ACTIVE
            }.minByOrNull {
                it.queueLength
            } ?: throw DeviceNotAvailableException(
                "Could not select a device because none matched the default predicates"
            )
        }
    }

    @Test
    @Order(7)
    fun getJobs_ok() {
        ibmqProvider.getJobs()
    }

    @Test
    @Order(8)
    fun getJobsLimit_ok() {
        ibmqProvider.getJobsLimit()
    }

    @Test
    @Order(9)
    fun runExperiment_request_ok() {
        val request = RUN_EXPERIMENT_REQUEST
        try {
            ibmqProvider.runExperiment(request)
        } catch (e: ClientRequestException) {
            if ("REACHED_JOBS_LIMIT" !in e.message) throw e
            else logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
        }
    }

    @Test
    @Order(10)
    fun runExperiment_program_ok() {
        val program = QASM_PROGRAM
        try {
            ibmqProvider.runExperiment(program)
        } catch (e: ClientRequestException) {
            if ("REACHED_JOBS_LIMIT" !in e.message) throw e
            else logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
        }
    }

    @Test
    @Order(11)
    fun updateExperiment_request_ok() {
        val request = VERSIONS_REQUEST
        ibmqProvider.updateExperiment(CODE_ID, request)
    }

    @Test
    @Order(12)
    fun updateExperiment_program_ok() {
        val program = QASM_PROGRAM
        ibmqProvider.updateExperiment(CODE_ID, program)
    }
}

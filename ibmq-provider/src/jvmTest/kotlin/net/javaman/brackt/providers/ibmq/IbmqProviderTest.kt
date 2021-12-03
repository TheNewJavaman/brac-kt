package net.javaman.brackt.providers.ibmq

import io.ktor.client.features.ClientRequestException
import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.TestData.NEW_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.RUN_EXPERIMENT_REQUEST
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IbmqProviderTest {
    private val ibmqProvider: IbmqProvider by injection()
    private val propertyManager: PropertyManager by injection()
    private val logger: Logger by injection()

    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    @Test
    @Order(1)
    fun logIn_ok() {
        val apiToken: String = propertyManager["IBMQ_API_TOKEN"]
        ibmqProvider.logIn(apiToken)
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
    fun newExperiment_ok() {
        val request = NEW_REQUEST
        ibmqProvider.newExperiment(request)
    }

    @Test
    @Order(8)
    fun getExperiments_ok() {
        ibmqProvider.getExperiments()
    }

    @Test
    @Order(9)
    fun updateExperiment_ok() {
        val codeId = ibmqProvider.code.idCode!!
        val request = ibmqProvider.code.copy(id = null)
        ibmqProvider.updateExperiment(codeId, request)
    }

    @Test
    @Order(10)
    fun getExperiment_ok() {
        val codeId = ibmqProvider.code.idCode!!
        ibmqProvider.getExperiment(codeId)
    }

    @Test
    @Order(11)
    fun runExperiment_ok() {
        val request = RUN_EXPERIMENT_REQUEST
        try {
            ibmqProvider.runExperiment(request)
        } catch (e: ClientRequestException) {
            if ("REACHED_JOBS_LIMIT" !in e.message) throw e
            else logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
        }
    }

    @Test
    @Order(12)
    fun getJobs_ok() {
        ibmqProvider.getJobs()
    }

    @Test
    @Order(13)
    fun getJobsLimit_ok() {
        ibmqProvider.getJobsLimit()
    }
}

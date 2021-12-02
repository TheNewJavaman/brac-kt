package net.javaman.brackt.providers.ibmq.api

import io.ktor.client.features.ClientRequestException
import kotlinx.coroutines.runBlocking
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.TestData.DEVICE
import net.javaman.brackt.providers.ibmq.TestData.GROUP
import net.javaman.brackt.providers.ibmq.TestData.LOG_IN_WITH_TOKEN_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.NETWORK
import net.javaman.brackt.providers.ibmq.TestData.PROJECT
import net.javaman.brackt.providers.ibmq.TestData.RUN_EXPERIMENT_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.VERSIONS_REQUEST
import net.javaman.brackt.providers.ibmq.TestUtil
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IbmqApiTest {
    private val propertyManager: PropertyManager by injection()
    private val ibmqApi: IbmqApi by injection()

    init {
        TestUtil.addInjections()
    }

    @Test
    @Order(1)
    fun logInWithToken_ok() {
        val request = LOG_IN_WITH_TOKEN_REQUEST
        val response = runBlocking { ibmqApi.logInWithToken(request) }

        propertyManager.setProperty("IBMQ_ACCESS_TOKEN", response.id)
        propertyManager.setProperty("IBMQ_USER_ID", response.userId)
    }

    @Test
    @Order(2)
    fun apiToken_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val userId = propertyManager.getProperty<String>("IBMQ_USER_ID")
        runBlocking { ibmqApi.apiToken(accessToken, userId) }
    }

    @Test
    @Order(3)
    fun networks_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        runBlocking { ibmqApi.networks(accessToken) }
    }

    @Test
    @Order(4)
    fun backends_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        runBlocking { ibmqApi.backends(accessToken) }
    }

    @Test
    @Order(5)
    fun jobs_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        runBlocking { ibmqApi.jobs(accessToken) }
    }

    @Test
    @Order(6)
    fun jobsLimit_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val network = NETWORK
        val group = GROUP
        val project = PROJECT
        val device = DEVICE
        runBlocking { ibmqApi.jobsLimit(accessToken, network, group, project, device) }
    }

    @Test
    @Order(7)
    fun runExperiment_ok() {
        val request = RUN_EXPERIMENT_REQUEST
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val network = NETWORK
        val group = GROUP
        val project = PROJECT
        try {
            runBlocking { ibmqApi.runExperiment(accessToken, request, network, group, project) }
        } catch (e: ClientRequestException) {
            // If too many tests have been run in a short period of time, IBM will rate-limit the API token
            if ("REACHED_JOBS_LIMIT" !in e.message) throw e
        }
    }

    @Test
    @Order(8)
    fun versions_ok() {
        val request = VERSIONS_REQUEST.copy(
            userId = propertyManager.getProperty("IBMQ_USER_ID")
        )
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val code = propertyManager.getProperty<String>("IBMQ_ID_CODE")
        runBlocking { ibmqApi.versions(accessToken, request, code) }
    }
}

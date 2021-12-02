package net.javaman.brackt.providers.ibmq.api

import io.ktor.client.features.ClientRequestException
import net.javaman.brackt.api.BracKtApi
import net.javaman.brackt.api.util.injections.injection
import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.properties.PropertyManager
import net.javaman.brackt.providers.ibmq.IbmqProvider
import net.javaman.brackt.providers.ibmq.TestData.CODE_ID
import net.javaman.brackt.providers.ibmq.TestData.DEVICE
import net.javaman.brackt.providers.ibmq.TestData.GROUP
import net.javaman.brackt.providers.ibmq.TestData.LOG_IN_WITH_TOKEN_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.NETWORK
import net.javaman.brackt.providers.ibmq.TestData.PROJECT
import net.javaman.brackt.providers.ibmq.TestData.RUN_EXPERIMENT_REQUEST
import net.javaman.brackt.providers.ibmq.TestData.VERSIONS_REQUEST
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IbmqApiTest {
    private val propertyManager: PropertyManager by injection()
    private val ibmqApi: IbmqApi by injection()
    private val logger: Logger by injection()

    init {
        BracKtApi.addInjections()
        IbmqProvider.addInjections()
    }

    @Test
    @Order(1)
    fun logInWithTokenSync_ok() {
        val request = LOG_IN_WITH_TOKEN_REQUEST
        val response = ibmqApi.logInWithTokenSync(request)

        propertyManager["TEST_IBMQ_ACCESS_TOKEN"] = response.id
        propertyManager["TEST_IBMQ_USER_ID"] = response.userId
    }

    @Test
    @Order(2)
    fun apiTokenSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        val userId: String = propertyManager["TEST_IBMQ_USER_ID"]
        ibmqApi.apiTokenSync(accessToken, userId)
    }

    @Test
    @Order(3)
    fun networksSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        ibmqApi.networksSync(accessToken)
    }

    @Test
    @Order(4)
    fun backendsSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        ibmqApi.backendsSync(accessToken)
    }

    @Test
    @Order(5)
    fun jobsSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        ibmqApi.jobsSync(accessToken)
    }

    @Test
    @Order(6)
    fun jobsLimitSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        val network = NETWORK
        val group = GROUP
        val project = PROJECT
        val device = DEVICE
        ibmqApi.jobsLimitSync(accessToken, network, group, project, device)
    }

    @Test
    @Order(7)
    fun runExperimentSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        val request = RUN_EXPERIMENT_REQUEST
        val network = NETWORK
        val group = GROUP
        val project = PROJECT
        try {
            ibmqApi.runExperimentSync(accessToken, request, network, group, project)
        } catch (e: ClientRequestException) {
            if ("REACHED_JOBS_LIMIT" !in e.message) throw e
            else logger.info { "Maximum number of concurrent jobs reached. You really gotta get a better API key..." }
        }
    }

    @Test
    @Order(8)
    fun versionsSync_ok() {
        val accessToken: String = propertyManager["TEST_IBMQ_ACCESS_TOKEN"]
        val userId: String = propertyManager["TEST_IBMQ_USER_ID"]
        val request = VERSIONS_REQUEST.copy(
            userId = userId
        )
        val code = CODE_ID
        ibmqApi.versionsSync(accessToken, request, code)
    }
}

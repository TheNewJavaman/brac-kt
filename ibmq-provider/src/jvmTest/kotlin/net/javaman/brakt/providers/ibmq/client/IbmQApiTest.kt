package net.javaman.brakt.providers.ibmq.client

import kotlinx.coroutines.runBlocking
import net.javaman.brakt.api.util.PropertyManager
import net.javaman.brakt.api.util.assert
import net.javaman.brakt.api.util.injection
import net.javaman.brakt.providers.ibmq.TestData.LOG_IN_WITH_TOKEN_REQUEST
import net.javaman.brakt.providers.ibmq.TestUtil
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class IbmQApiTest {
    private val propertyManager: PropertyManager by injection()

    init {
        TestUtil.addInjections()
    }

    @Test
    @Order(1)
    fun logInWithToken_ok() {
        val request = LOG_IN_WITH_TOKEN_REQUEST
        val response = runBlocking { IbmQApi.logInWithToken(request) }
        assert { response.id.isNotBlank() }
        propertyManager.setProperty("IBMQ_ACCESS_TOKEN", response.id)
        propertyManager.setProperty("IBMQ_USER_ID", response.userId)
    }

    @Test
    @Order(2)
    fun getApiToken_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val userId = propertyManager.getProperty<String>("IBMQ_USER_ID")
        val response = runBlocking { IbmQApi.getApiToken(accessToken, userId) }
        assert { response.apiToken.isNotBlank() }
    }

    @Test
    @Order(3)
    fun getNetwork_ok() {
        val accessToken = propertyManager.getProperty<String>("IBMQ_ACCESS_TOKEN")
        val response = runBlocking { IbmQApi.getNetwork(accessToken) }
        assert { response.isNotEmpty() }
    }
}

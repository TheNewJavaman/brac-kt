package net.javaman.brakt.providers.ibmq

import net.javaman.brakt.api.util.PropertyManager
import net.javaman.brakt.api.util.injection
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenRequest

object TestData {
    private val propertyManager: PropertyManager by injection()

    @JvmStatic
    val LOG_IN_WITH_TOKEN_REQUEST = LogInWithTokenRequest(
        apiToken = propertyManager.getProperty("IBMQ_API_TOKEN")
    )
}

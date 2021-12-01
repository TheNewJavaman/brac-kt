package net.javaman.brackt.providers.ibmq

import net.javaman.brakt.api.util.properties.PropertyManager
import net.javaman.brakt.api.util.injections.injection
import net.javaman.brackt.providers.ibmq.client.models.LogInWithTokenRequest

object TestData {
    private val propertyManager: PropertyManager by injection()

    @JvmStatic
    val LOG_IN_WITH_TOKEN_REQUEST = LogInWithTokenRequest(
        apiToken = propertyManager.getProperty("IBMQ_API_TOKEN")
    )
}

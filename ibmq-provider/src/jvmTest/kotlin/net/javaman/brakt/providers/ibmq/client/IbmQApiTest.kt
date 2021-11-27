package net.javaman.brakt.providers.ibmq.client

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import net.javaman.brakt.api.util.getProperty
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenRequest
import net.javaman.brakt.providers.ibmq.client.models.LogInWithTokenResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class IbmQApiTest {
    @Test
    fun logInWithToken_ok() {
        val request = LogInWithTokenRequest(
            apiToken = getProperty("IBMQ_APITOKEN")
        )
        val expectedResponse = LogInWithTokenResponse(
            id = "",
            ttl = 1209600,
            created = Instant.DISTANT_PAST,
            userId = ""
        )
        val actualResponse = runBlocking { IbmQApi.logInWithToken(request) }
        assertEquals(expectedResponse.ttl, actualResponse.ttl)
    }
}

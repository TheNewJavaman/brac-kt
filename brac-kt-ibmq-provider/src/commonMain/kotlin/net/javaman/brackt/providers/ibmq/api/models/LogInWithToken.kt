@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Serializable
data class LogInWithTokenRequest(
    val apiToken: String
)

@JsExport
@Serializable
data class LogInWithTokenResponse(
    val id: String,
    val ttl: Long,
    val created: Instant,
    val userId: String
)

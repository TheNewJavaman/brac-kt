@file:Suppress("MatchingDeclarationName") // Keep the naming convention of the package
@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Serializable
data class LastestResponse(
    val total: Int,
    val count: Int,
    val codes: List<Code>
)

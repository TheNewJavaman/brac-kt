@file:OptIn(ExperimentalJsExport::class)

package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

typealias BackendsResponse = List<BackendResponse>

@JsExport
@Serializable
data class BackendResponse(
    val hasAccess: Boolean,
    val internalId: String? = null,
    val name: String,
    val accessType: String? = null,
    val basisGates: List<String>,
    val couplingMap: List<List<Int>>? = null,
    val backendVersion: String,
    val qubits: Int,
    val isSimulator: Boolean,
    val quantumVolume: Int? = null,
    val clops: Int? = null,
    val processorType: BackendProcessorType,
    val revision: Double? = null,
    val deviceStatus: BackendDeviceStatus,
    val queueLength: Int,
    val inputAllowed: List<String>,
    val category: String? = null
)

@JsExport
@Serializable
data class BackendProcessorType(
    val family: String,
    val description: String? = null,
    val revision: Double? = null,
    val segment: String? = null
)

@JsExport
@Serializable
data class BackendDeviceStatus(
    val state: Boolean,
    val status: String,
    val message: String
)

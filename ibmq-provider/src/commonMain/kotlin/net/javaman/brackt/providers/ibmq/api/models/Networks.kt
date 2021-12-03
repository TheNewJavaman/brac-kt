package net.javaman.brackt.providers.ibmq.api.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

typealias NetworksResponse = List<NetworkResponse>

@Serializable
data class NetworkResponse(
    val name: String,
    val title: String,
    val description: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val private: Boolean,
    val licenseNotRequired: Boolean,
    val isDefault: Boolean,
    val analytics: Boolean,
    @SerialName("class")
    val classEnum: String,
    val id: String,
    val ownerId: String,
    val device: List<String>,
    val priority: Int,
    val groups: Map<String, NetworkGroupResponse>,
)

@Serializable
data class NetworkGroupResponse(
    val name: String,
    val title: String,
    val isDefault: Boolean,
    val description: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val projects: Map<String, NetworkProjectResponse>
)

@Serializable
data class NetworkProjectResponse(
    val name: String,
    val title: String,
    val isDefault: Boolean,
    val description: String,
    val creationDate: Instant,
    val deleted: Boolean,
    val devices: Map<String, NetworkDeviceResponse>,
    val users: Map<String, @Contextual Any>
)

@Serializable
data class NetworkDeviceResponse(
    val priority: Int,
    val name: String,
    val deleted: Boolean,
    val specificConfiguration: Map<String, JsonElement>? = null,
    val configuration: NetworkConfigurationResponse? = null
)

@Serializable
data class NetworkConfigurationResponse(
    val limit: Int? = null,
    val capabilities: Map<String, JsonElement>
)

package net.javaman.brackt.api.util.properties

/**
 * JS implementation of [PropertyManager]. Reads environment variables from nodejs in addition to the runtime properties
 */
actual class PropertyManager {
    val customProperties = mutableMapOf<String, Any>()

    actual inline operator fun <reified T : Any> get(key: String): T = customProperties[key] as? T
        ?: js("process.env[key]") as? T
        ?: throw UninitializedPropertyException("Could not find key ($key)")

    actual operator fun <T : Any> set(key: String, value: T) {
        customProperties[key] = value
    }
}

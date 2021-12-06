package net.javaman.brackt.api.util.properties

/**
 * JVM implementation of [PropertyManager]. Reads environment variables in addition to the runtime properties
 */
actual class PropertyManager {
    val customProperties = mutableMapOf<String, Any>()

    actual inline operator fun <reified T : Any> get(key: String): T {
        val allProperties = mutableMapOf<String, Any>()
        allProperties.putAll(customProperties)
        allProperties.putAll(System.getenv())
        val value = allProperties[key] ?: throw UninitializedPropertyException("Could not find key ($key)")
        return value as T
    }

    actual operator fun <T : Any> set(key: String, value: T) {
        customProperties[key] = value
    }
}

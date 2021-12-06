package net.javaman.brackt.api.util.properties

/**
 * Manages a shared map of properties. Each platform should have its own implementation
 */
expect class PropertyManager() {
    inline operator fun <reified T : Any> get(key: String): T

    operator fun <T : Any> set(key: String, value: T)
}

class UninitializedPropertyException(override val message: String) : Exception(message)

package net.javaman.brackt.api.util.properties

/**
 * Manages a shared map of properties. Each platform should have its own implementation
 */
expect class PropertyManager {
    inline fun <reified T : Any> getProperty(key: String): T

    fun <T : Any> setProperty(key: String, value: T)
}

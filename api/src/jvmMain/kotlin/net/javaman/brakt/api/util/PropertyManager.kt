package net.javaman.brakt.api.util

actual class PropertyManager {
    val customProperties = mutableMapOf<String, Any>()

    actual inline fun <reified T : Any> getProperty(key: String): T {
        val allProperties = mutableMapOf<String, Any>()
        allProperties.putAll(customProperties)
        allProperties.putAll(System.getenv())
        val value = allProperties[key] ?: throw UninitializedPropertyException("Could not find key=$key")
        return value as T
    }

    actual fun <T : Any> setProperty(key: String, value: T) {
        customProperties[key] = value
    }
}

package net.javaman.brakt.api.util

expect class PropertyManager {
    inline fun <reified T : Any> getProperty(key: String): T

    fun <T : Any> setProperty(key: String, value: T)
}

class UninitializedPropertyException(override val message: String) : Exception(message)

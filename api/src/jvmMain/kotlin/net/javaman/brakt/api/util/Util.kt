package net.javaman.brakt.api.util

actual inline fun <reified T> getProperty(key: String): T {
    val allProperties = mutableMapOf<String, Any>()
    allProperties.putAll(System.getenv())
    val value = allProperties[key] ?: throw UninitializedPropertyAccessException()
    if (value is T) return value
    else throw TypeCastException()
}

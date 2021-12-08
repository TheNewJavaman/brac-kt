package net.javaman.brackt.api.util.reflection

import kotlin.reflect.KClass

/**
 * Qualified name reflection is not supported by Kotlin/JS
 */
actual fun KClass<*>.getPlatformName() = this.simpleName ?: ANONYMOUS_NAME

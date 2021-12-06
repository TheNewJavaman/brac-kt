package net.javaman.brackt.api.util.reflection

import kotlin.reflect.KClass

actual fun KClass<*>.getPlatformQualifiedName() = this.qualifiedName ?: "Anonymous"

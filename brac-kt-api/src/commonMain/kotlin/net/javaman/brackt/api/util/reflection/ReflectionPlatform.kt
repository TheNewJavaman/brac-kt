package net.javaman.brackt.api.util.reflection

import kotlin.reflect.KClass

expect fun KClass<*>.getPlatformName(): String

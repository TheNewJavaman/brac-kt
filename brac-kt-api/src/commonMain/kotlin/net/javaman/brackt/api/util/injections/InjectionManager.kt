package net.javaman.brackt.api.util.injections

import net.javaman.brackt.api.util.logging.Logger
import net.javaman.brackt.api.util.reflection.getPlatformName
import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Manages dependency injections in pure Kotlin and a clean syntax. Useful for multiplatform projects
 *
 * Example:
 * ```kotlin
 * InjectionManager.add {
 *     one { PropertyManager() }
 *     one { IbmqApi() }
 *     many { Logger(it) }
 * }
 * ```
 * There are two shared [PropertyManager] and [IbmqApi] instances, but each [Logger] instance has its own class
 * reference
 */
class InjectionManager {
    companion object {
        @JvmStatic
        val oneInstanceList = mutableListOf<Any>()

        @JvmStatic
        val manyInstanceMap = mutableMapOf<KClass<*>, (KClass<*>) -> Any>()

        @JvmStatic
        fun add(block: InjectionManager.() -> Unit) = block(InjectionManager())
    }

    fun one(block: () -> Any) {
        val instance = block()
        if (oneInstanceList.none { it::class.isInstance(instance) }) oneInstanceList.add(instance)
    }

    inline fun <reified T : Any> many(noinline block: (KClass<*>) -> T) {
        if (manyInstanceMap.keys.none { it == T::class}) {
            manyInstanceMap[T::class] = block
        }
    }

    inline operator fun <reified T> getValue(thisRef: Any, property: KProperty<*>): T {
        return manyInstanceMap.keys.firstOrNull { it == T::class }?.let {
            manyInstanceMap[it]!!.invoke(thisRef::class) as T
        } ?: oneInstanceList.firstOrNull { T::class.isInstance(it) } as? T
        ?: throw UninitializedInjectionException("${T::class.getPlatformName()} has not yet been initialized")
    }
}

/**
 * Syntactic sugar for [InjectionManager]
 */
fun injection() = InjectionManager()

class UninitializedInjectionException(override val message: String) : Exception(message)

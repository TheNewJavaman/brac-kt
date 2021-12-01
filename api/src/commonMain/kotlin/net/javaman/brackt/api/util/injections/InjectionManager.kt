package net.javaman.brackt.api.util.injections

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
 *     many { Logger.fromKClass(it) }
 * }
 * ```
 * There is one shared [PropertyManager] instance, but each [Logger] instance has its own class reference
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
        if (manyInstanceMap.keys.none { it == T::class }) manyInstanceMap[T::class] = block
    }

    inline operator fun <reified T> getValue(thisRef: Any, property: KProperty<*>): T {
        return manyInstanceMap.keys.firstOrNull { it == T::class }?.let {
            manyInstanceMap[it]!!.invoke(thisRef::class) as T
        } ?: oneInstanceList.firstOrNull { T::class.isInstance(it) } as? T
        ?: throw UninitializedInjectionException("")
    }
}

/**
 * Syntactic sugar for [InjectionManager]
 */
fun injection() = InjectionManager()

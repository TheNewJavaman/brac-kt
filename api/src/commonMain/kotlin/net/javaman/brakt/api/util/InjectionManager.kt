package net.javaman.brakt.api.util

import kotlin.jvm.JvmStatic
import kotlin.reflect.KProperty

class InjectionManager {
    companion object {
        @JvmStatic
        val dependencies = mutableListOf<Any>()

        @JvmStatic
        fun addAll(block: InjectionManager.() -> Unit) = block(InjectionManager())
    }

    fun dependency(block: () -> Any) {
        val instance = block()
        if (dependencies.none { it::class.isInstance(instance) }) dependencies.add(instance)
    }

    inline operator fun <reified T> getValue(thisRef: Any, property: KProperty<*>): T {
        return dependencies.firstOrNull { T::class.isInstance(it) } as T
    }
}

fun injection() = InjectionManager()
